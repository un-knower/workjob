package sparkdemo.firstwork

import java.text.SimpleDateFormat
import java.util.Date

import org.apache.spark.SparkContext
import org.apache.spark.rdd.RDD
import org.apache.spark.sql.{DataFrame, Row, SparkSession}
import redis.clients.jedis.JedisPool
import util.Job
import util.JobConfigDef._
import com.redislabs.provider.redis._

/*
* 陪玩实时UV PV 统计
* */
object RealtimeUPV extends Job {
    private var ss: SparkSession = _
    private var sc: SparkContext = _
    private var pool: JedisPool = _

    def main(args: Array[String]): Unit = {
        val config = if (args.length != 0) args(0) else "monitor/RealtimeUPV.properties"
        configs = initDriver(config)
        require(configs.nonEmpty, s"job configs does'nt be null : ${configs.mkString("\n")}")
        /*一个来源于工具类的方法  startStreaming 这个方法封装了offset的保存 */
        startStreaming(process)
    }

    def initDriver(config: String) = {
        val configs = initJobConf(config)
        pool = new JedisPool(configs("redis.host"), configs("redis.port").toInt)
        val sparkSession = getSparkSession(configs(JOB_NAME), configs.toMap)
        paramsContainer += ("sparkSession" -> sparkSession)
        ss = sparkSession
        sc = sparkSession.sparkContext
        configs
    }

    def process(rdd: RDD[(String, String)]): Unit = {
        println("数据处理开始")
        var now: Date = new Date()
        var dateFormat: SimpleDateFormat = new SimpleDateFormat("yyyy-MM-dd")
        var day = dateFormat.format(now)
        val DF = ss.read.json(rdd.map(_._2))
        DF.createOrReplaceTempView("app")
        /* 统计UV 和 PV */
        val upv:DataFrame = ss.sql("select uuid as uid,count(*) as cnt from app group by uuid")
        upv.foreachPartition(partition => {
            val client = pool.getResource
            val pip = client.pipelined()
            partition.foreach(row => {
                val uid = row.getString(0)
                val cnt = row.getLong(1)
                pip.setbit(configs("uv") + s":${day}", uid.hashCode, true)
                pip.incrBy(configs("pv") + s":${day}", cnt)
            })
            pip.sync()
            pip.clear()
            pip.close()
            client.close()
        })
        val statics = ss.sql(
            """
        |select room_id,count(*) as cnt from(
        |select
        |   case
        |      when cd rlike '^room_\\d+$' then substr(cd, 6)
        |      when cd rlike '^sproom_\\d+$' then substr(cd, 8)
        |      else ceil(256*rand())*(-1)
        |   end as room_id,city
        |from app
        )t where room_id >0 group by room_id
      """.stripMargin)
        val s: RDD[(String, Long)] = statics.rdd.map(row => {
            (row.getString(0), row.getLong(1))
        })
        /* 将统计结果进行维护  不选择spark自带的维护  测试累加器和redis */
        val s1: RDD[(String, Long)] = sc.fromRedisHash(configs("room")).map(x => {
            (x._1, x._2.toLong)
        })
        val result: RDD[(String, String)] = s.fullOuterJoin(s1).map(x => {
            val key = x._1
            val value = x._2._1.getOrElse[Long](0) + x._2._2.getOrElse[Long](0)
            (key, value.toString)
        })
        sc.toRedisHASH(result, configs("room"))
    }

}

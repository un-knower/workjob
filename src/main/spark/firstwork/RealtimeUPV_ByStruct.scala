package sparkdemo.firstwork

import _root_.util.JobConfigDef._
import _root_.util.StructJob
import org.apache.spark.SparkContext
import org.apache.spark.sql._
import org.apache.spark.sql.streaming.{StreamingQuery, Trigger}
import redis.clients.jedis.JedisPool
import stream.util.JDBCSink


/*
* 陪玩实时UV PV 统计—— 结构化流完成
*
* */

object RealtimeUPV_ByStruct extends StructJob {
    var sparkSession: SparkSession = _
    var sc: SparkContext = _
    var pool: JedisPool = _

    def main(args: Array[String]): Unit = {
        val configpath = if (args.length != 0) args(0) else "monitor/RealtimeUPV_ByStruct.properties"
        initDriver(configpath)
        startStreaming(process)
    }

    def initDriver(path: String): Unit = {
        initJobConf(path)
        sparkSession = getSparkSession(configs(JOB_NAME), configs.toMap)
        sc=sparkSession.sparkContext
        paramsContainer += ("sparkSession" -> sparkSession)
    }

    def process(DF: DataFrame): StreamingQuery = {
        val im = sparkSession.implicits
        println("开始处理数据")
        DF.createOrReplaceTempView("app")
        val statics = sparkSession.sql(
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
        val writer = new JDBCSink("jdbc:mysql://localhost:3306/kingcall?&serverTimezone=UTC&characterEncoding=utf8&useSSL=false","root", "www1234")
       // val query = statics.writeStream.trigger(Trigger.ProcessingTime("300 seconds")).outputMode("complete").format("console").start()
        /* 带来的问题就是数据库里的数据有重复,在update 的模式下也不行，更新模式更新的是自己内存中维护的数据，不包括输出后的数据,所以更新得由自己做*/

        statics.writeStream.trigger(Trigger.ProcessingTime("60 seconds")).outputMode("update").foreach(writer).start()

    }

}

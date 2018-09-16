package sparkdemo.firstwork.hbasepack

import java.util.{Date, UUID}

import com.alibaba.fastjson.JSON
import kafka.serializer.StringDecoder
import org.apache.hadoop.hbase.client.{ConnectionFactory, Put}
import org.apache.hadoop.hbase.util.Bytes
import org.apache.hadoop.hbase.{HBaseConfiguration, TableName}
import org.apache.spark.streaming.{Seconds, StreamingContext}
import org.apache.spark.{SparkConf, SparkContext}
import org.apache.spark.streaming.kafka.KafkaUtils
import sparkdemo.firstwork.Jobinit


object StreamingHbase extends Jobinit {

    def main(args: Array[String]) {
        val sparkConf = new SparkConf().setAppName("KafkaAndHbase").setMaster("local[2]")
        val sc = new SparkContext(sparkConf)
        sc.setLogLevel("error")
        val scc = new StreamingContext(sc, Seconds(5))
        val topicSet = Set("mbchat")
        val kafkaParams = Map[String, String]("metadata.broker.list" -> "master:9092,slave1:9092,slave2:9092"
            , "group.id" -> "directs"
        )
        val rmessages = KafkaUtils.createDirectStream[String, String, StringDecoder, StringDecoder](scc, kafkaParams, topicSet)
        rmessages.foreachRDD(rdd => {
            println("rdd 中的数据条数:" + rdd.count())
            if (!rdd.isEmpty()) {
                val columns = "category,update_time,word,index_value,platform_name,platform,record_id,day".split(",")
                val keys = "fromUserId,createTime".split(",")
                val CF = Bytes.toBytes("info")
                println(rdd)
                rdd.foreachPartition(partititon => {
                    val conf = HBaseConfiguration.create()
                    conf.addResource("hbase.xml")
                    val conn = ConnectionFactory.createConnection(conf)
                    val userTable = TableName.valueOf("spark")
                    val table = conn.getTable(userTable)
                    /*将每一天数据解析放在 hbase中   欠缺两个东西  一个是hbase 连接池  一个是批量插入*/
                    partititon.foreach(record => {
                        val tmpobj = JSON.parseObject(record._2)
                        var rowkey = UUID.randomUUID().toString.replaceAll("-", "")
                        val put = new Put(Bytes.toBytes(rowkey))
                        for (cl <- columns) {
                            put.addColumn(CF, Bytes.toBytes(cl), Bytes.toBytes(tmpobj.getString(cl)))
                        }
                        table.put(put)
                    })
                    table.close()
                    conn.close()
                })
            }
        })
        scc.start()
        scc.awaitTermination()
    }
}

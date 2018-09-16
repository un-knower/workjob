package sparkdemo.firstwork

import kafka.serializer.StringDecoder
import org.apache.spark.streaming.kafka.KafkaUtils
import org.apache.spark.streaming.{Seconds, StreamingContext}
import org.apache.spark.{SparkConf, SparkContext}

/**
  * Created by Administrator on 2018/2/24.
  */
object DirectGetDataForKafka {

    def main(args: Array[String]): Unit = {
        getData
    }

    def getData: Unit = {
        val sparkConf = new SparkConf().setMaster("local[2]").setAppName("kafkaStreaming")
        val sc = new SparkContext(sparkConf)
        val ssc = new StreamingContext(sc, Seconds(5))
        val topicSet = Set("kp")
        val kafkaParams = Map[String, String]("metadata.broker.list" -> "ts:9092"
            , "group.id" -> "direct"
        )
        val rmessages = KafkaUtils.createDirectStream[String, String, StringDecoder, StringDecoder](ssc, kafkaParams, topicSet)

        rmessages.foreachRDD(rdd => {
            rdd.persist()
            rdd.foreach(print)
            println("-------------------------------------------------")
        })
        ssc.start()
        ssc.awaitTermination()
    }
}

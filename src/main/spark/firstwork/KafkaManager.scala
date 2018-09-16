package sparkdemo.firstwork

import kafka.common.TopicAndPartition
import kafka.message.MessageAndMetadata
import kafka.serializer.Decoder
import org.apache.spark.SparkException
import org.apache.spark.rdd.RDD
import org.apache.spark.streaming.StreamingContext
import org.apache.spark.streaming.dstream.InputDStream
import org.apache.spark.streaming.kafka.{HasOffsetRanges, KafkaCluster, KafkaUtils}

import scala.collection.immutable.HashMap
import scala.reflect.ClassTag

/**
  * 一个简单的维护offset的类  是通过kafka的一个API来维护的
  * KafkaCluster类用于建立和Kafka集群的链接相关的操作工具类，我们可以对Kafka中Topic的每个分区设置其相应的偏移量
  * Map((topicAndPartition, offsets.untilOffset)),然后调用KafkaCluster类的setConsumerOffsets方法去更新Zookeeper里面的信息，这样我们就可以更新Kafka的偏移量，
  *
  * @param kafkaParams
  */
class KafkaManager(val kafkaParams: Map[String, String]) extends Serializable {

    private val kc = new KafkaCluster(kafkaParams)

    /**
      * 创建数据流 通过此方法创建的流是每次都会去zk上查询offset然后创建对应的流
      * 发现最终还是调用 KafkaUtils.createDirectStream 来创建的，也就是说这个方法读取了offset
      * 获取到offset则利用offset，获取不到offset则直接使用没有offset的方式创建
      * 其实kafkautils是有一个获取offset的方法的，但是里面也是调用的kc的相关方法，所有涉及到的一切都是可以通过调用kafkautils完成的,但是这些方法是私有的
      * 和无offset创建流的区别是：
      * 无offset则从partition的leader获取最新或者最早的topic,否则则是利用获取到的offset
      *
      * 一个可用的工具应该满足：
      * 获取到则使用，获取不到则根据参数获取最新或者最早（根据参数）
      *
      * @param ssc
      * @param kafkaParams
      * @param topics
      * @tparam K
      * @tparam V
      * @tparam KD
      * @tparam VD
      * @return
      */
    def createDirectStream[K: ClassTag, V: ClassTag, KD <: Decoder[K] : ClassTag, VD <: Decoder[V] : ClassTag](ssc: StreamingContext, kafkaParams: Map[String, String], topics: Set[String]): InputDStream[(K, V)] = {
        val groupId = kafkaParams.get("group.id").get
        //从zookeeper上读取offset开始消费message,如果是left则抛出异常这和源码对应
        val partitionsE = kc.getPartitions(topics)
        if (partitionsE.isLeft)
            throw new SparkException(s"get kafka partition failed: ${partitionsE.left.get}")
        /*TopicAndPartition 对象的set集合*/
        val partitions = partitionsE.right.get
        val consumerOffsetsE = kc.getConsumerOffsets(groupId, partitions)
        /*对获取到的offset也进行了判断是不是 right */
        if (!consumerOffsetsE.isLeft) {
            val consumerOffsets = consumerOffsetsE.right.get
            KafkaUtils.createDirectStream[K, V, KD, VD, (K, V)](
                ssc, kafkaParams, consumerOffsets, (mmd: MessageAndMetadata[K, V]) => (mmd.key, mmd.message))
        } else {
            KafkaUtils.createDirectStream(ssc, kafkaParams, topics)
        }
    }


    /**
      * 更新zookeeper上的消费offsets
      *
      * @param rdd
      */
    def updateZKOffsets(rdd: RDD[(String, String)]): Unit = {
        val groupId = kafkaParams.get("group.id").get
        val offsetsList = rdd.asInstanceOf[HasOffsetRanges].offsetRanges
        // OffsetRange(topic: 'oso', partition: 0, range: [581 -> 583]) 其实对一个RDD而言一个这样的对象

        for (offsets <- offsetsList) {
            val topicAndPartition = TopicAndPartition(offsets.topic, offsets.partition)
            val o = kc.setConsumerOffsets(groupId, HashMap((topicAndPartition, offsets.untilOffset)))
            if (o.isLeft) {
                println(s"Error updating the offset to Kafka cluster: ${o.left.get}")
            }
        }
    }


}



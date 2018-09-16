package sparkdemo.firstwork

import java.util.Locale

import com.alibaba.fastjson.JSON
import kafka.common.TopicAndPartition
import kafka.message.MessageAndMetadata
import kafka.serializer.StringDecoder
import org.apache.spark.SparkContext
import org.apache.spark.rdd.RDD
import org.apache.spark.sql.SparkSession
import org.apache.spark.storage.StorageLevel
import org.apache.spark.streaming.{Seconds, StreamingContext}
import org.apache.spark.streaming.dstream.{InputDStream, ReceiverInputDStream}
import org.apache.spark.streaming.kafka.{HasOffsetRanges, KafkaCluster, KafkaUtils}
import sparkdemo.firstwork.CombineStreaming.zkQuorum
import util.ZKUtils

object DirectOffsetStore {
    val sparkSession = SparkSession.builder().appName("test").master("local[2]").getOrCreate()
    val sc = sparkSession.sparkContext
    val tmpdir = "src/main/resources/chechpoint"
    val zkQuorum = "localhost:2181"
    val bkQuorum = "localhost:9092"

    def main(args: Array[String]): Unit = {
        saveOffsetToZK(sc)
    }

    /**
      * 有消费组信息，但是发现但是发现offset节点下并没有对应主题的offset值，而且存在着随机现象
      * 测试高阶API 对offset的维护,发现并没有去维护，从最新的offset去读 也就是说没有发送的时候消费之没有办法读到数据
      * 测试：是不是由于没有checkpoint导致的无法维护offset
      */
    def offsetAboutAuto(sc: SparkContext): Unit = {
        val scc = new StreamingContext(sc, Seconds(2))
        /*关于kafka的参数分开写，在里面依然会构建成一个map*/
        val kafkaParams = Map(
            "metadata.broker.list" -> bkQuorum,
            "group.id" -> "nodirectGP",
            "zookeeper.connect" -> zkQuorum,
            "auto.offset.reset" -> "smallest"
        )
        /*  map[topicname,partitions]  这个参数支持了多个topic    而且经过时间发现 [String, String, StringDecoder, StringDecoder] 这个玩意是比较重要的*/
        val input = KafkaUtils.createStream[String, String, StringDecoder, StringDecoder](scc, kafkaParams, Map("king" -> 1), StorageLevel.MEMORY_ONLY)
        input.foreachRDD(rdd => {
            rdd.foreach(println _)
        })
        scc.start()
        scc.awaitTermination()
    }

    /**
      * direct模式对offset的影响
      * 不止没有在zookeeper上保留offset信息 连 消费组信息都没有,每次都从最新的地方开始读,所以这种模式必须手动维护offset
      */
    def offsetAboutDirect(sc: SparkContext): Unit = {
        val kafkaParams = Map(
            "metadata.broker.list" -> "localhost:9092"
        )
        val topics = Set("king")
        /*发现StreamingContext 对象创建的时候可以将checkpoint对象传入，也就是说本身是支持恢复的*/
        val scc = new StreamingContext(sc, Seconds(2))
        /*发现这个方法其实支持 fromOffsets: Map[TopicAndPartition, Long] 这个对象的，也就是说只要从zk上读取offset即可*/
        val input = KafkaUtils.createDirectStream[String, String, StringDecoder, StringDecoder](scc, kafkaParams, topics)
        input.foreachRDD(rdd => {
            rdd.foreach(println _)
        })
        scc.start()
        scc.awaitTermination()
        scc.stop(false)
    }


    def test1(scc: StreamingContext): Unit = {

        /*数据检查点，当发生死机时，可从上次失败的地方继续执行-------------->如何验证,还有就是这句代码所处的位置*/
        val inputRdd: ReceiverInputDStream[(String, String)] = KafkaUtils.createStream(scc, zkQuorum, "user-behavior-topic-message-consumer-group", Map("longzhuresty" -> 1)
            , StorageLevel.MEMORY_ONLY)

        /*
          第一个参数是StreamingContext实例;
          第二个参数是ZooKeeper集群信息(接受Kafka数据的时候会从ZooKeeper中获得Offset等元数据信息)
          第三个参数是Consumer Group
          第四个参数是消费的Topic以及并发读取Topic中Partition的线程数
        */

        /*对接收到的数据进行判断处理  新的问题spark中流处理到底是从哪里开始循环的     发现这个方法只被调用了一次*/

        /* val path:String=SaveToHDFS.createFolderAndGetPath()*/

        inputRdd.foreachRDD(rdd => {
            if (rdd.count() > 0) {
                var name = ""
                /*如果经常在每间隔5秒钟没有数据的话不断的启动空的Job其实是会造成调度资源的浪费，因为并没有数据需要发生计算，所以实例的企业级生成环境的代码在具体提交Job前会判断是否有数据，如果没有的话就不再提交Job*/
                rdd.foreach(x => {
                    var tmpobj = JSON.parseObject(x._2)
                    name = tmpobj.get("host") + ""
                    println("---------------" + name)
                    var conetxt: String = tmpobj.get("client_ip") + "\t" + tmpobj.get("is_blocked") + "\t" + tmpobj.get("args") + "\t" + tmpobj.get("status") + "\t" + tmpobj.get("uid") + "\t" + name + "\r\n"
                    SaveToLocal.saveFile(SaveToLocal.createFolderAndGetPath, conetxt, name)
                    tmpobj
                })
            }
        })
        /*可以避免一个RDD有多个分区，有合并分区的意思*/
        /*inputRdd.repartition(1).saveAsTextFiles("C:\\Users\\PLUSH80702\\Desktop\\receive\\")*/
        scc.start()
        scc.awaitTermination()
        scc.stop()

    }

    /**
      * 测试从zk的上读取offset然后创建streaming,然后保存offset到zk
      *   1.存在的时候则读取，不存在的时候则从最新的读取（能不能从头——不是0而是当前最小offset）
      *
      * @param sc
      */
    def saveOffsetToZK(sc: SparkContext): Unit = {
        val kafkaParams = Map(
            "metadata.broker.list" -> bkQuorum,
            "group.id" -> "directGP",
            "zookeeper.connect" -> zkQuorum
        )
        val topics = Set("app_log_screenview2")
        println(topics)
        val kafkaManager: KafkaManager = new KafkaManager(kafkaParams)
        val scc = new StreamingContext(sc, Seconds(2))
        val input = kafkaManager.createDirectStream[String, String, StringDecoder, StringDecoder](scc, kafkaParams, topics)
        input.foreachRDD(rdd => {
            rdd.foreach(println _)
            kafkaManager.updateZKOffsets(rdd)
        })
        scc.start()
        scc.awaitTermination()
        scc.stop(false)
    }

    def saveOffsetToCheckPoint(sc: SparkContext): Unit = {
        val ssc = StreamingContext.getOrCreate(tmpdir + "/test1", createStreamingContext)
        ssc.start()
        ssc.awaitTermination()
        ssc.stop(false)
    }

    /*将创建streamingcontext对象和业务逻辑放在了一起;这不是一个好的选择，缺陷太多1.io  2. 逻辑  3. 大量小文件*/
    def createStreamingContext(): StreamingContext = {
        val kafkaParams = Map(
            "metadata.broker.list" -> bkQuorum,
            "group.id" -> "checkpoint",
            "zookeeper.connect" -> zkQuorum
        )
        val topics = Set("check")
        val ssc = new StreamingContext(sc, Seconds(2))
        ssc.checkpoint(tmpdir + "/test1")
        val input = KafkaUtils.createDirectStream[String, String, StringDecoder, StringDecoder](ssc, kafkaParams, topics)
        input.foreachRDD(rdd => {
            rdd.cache()
            rdd.checkpoint()
            rdd.foreach(println _)
            rdd.cache()
            rdd.checkpoint()
        })
        ssc
    }

    /**
      * 将kafkautils 中没有offset创建流的方法中的获取offset的方法直接copy出来,研究一些方法
      *
      * @param sc
      */
    def createDirectMineStream(sc: SparkContext): Unit = {
        val kafkaParams = Map(
            "metadata.broker.list" -> bkQuorum,
            "group.id" -> "directGP",
            "zookeeper.connect" -> zkQuorum
        )
        val kc = new KafkaCluster(kafkaParams)
        val topics = Set("oftest")
        val tmp = getOffsetsFromParams(kc, kafkaParams, topics)
        println(tmp)
    }

    /**
      * 其实这个方法是根据kafkaParams获取最新流或者最早流的,后台默认从最新的获取
      *   1.要改造如果获取到offset
      *     1.offset没过期则使用
      *     2.offset已经过期则使用最早的
      *   2.获取不到
      *     1.读取最新数据
      *     2.读取最老数据（其实这个更合理一点）
      *
      *
      * 对比维护offset工具类的方法
      *   1.先获取然后判断，如果不是right则抛出异常
      *   2.KafkaManager 先判断后获取
      *
      * @param kc
      * @param kafkaParams
      * @param topics
      * @return Map([oftest,0] -> 8, [oftest,1] -> 6, [oftest,2] -> 7)   本质上就是一个这样的对象
      */
    def getOffsetsFromParams(kc: KafkaCluster, kafkaParams: Map[String, String], topics: Set[String]): Map[TopicAndPartition, Long] = {
        val reset = kafkaParams.get("auto.offset.reset").map(_.toLowerCase(Locale.ROOT))
        println(reset)
        println(kc.getPartitions(topics))
        println(kc.getPartitions(topics).right)
        println(kc.getPartitions(topics).right.get)
        //Right(Map([oftest,0] -> 8, [oftest,1] -> 6, [oftest,2] -> 7))
        val result =
            for {
                topicPartitions <- kc.getPartitions(topics).right
                leaderOffsets <- (
                        if (reset == Some("smallest")) {
                            kc.getEarliestLeaderOffsets(topicPartitions)
                        }
                        else {
                            kc.getLatestLeaderOffsets(topicPartitions)
                        }
                        ).right
            }
                yield {
                    leaderOffsets.map { case (tp, lo) =>
                        (tp, lo.offset)
                    }
                }
        /*这里就是最终要的结果*/
        println(result.right.get)
        /*这里判断是不是right,并将结果返回*/
        KafkaCluster.checkErrors(result)
    }


    /**
      * 有缺陷的代码，只有维护,没有读取，但是这个代码可以搞清楚offset维护的根本是什么
      */

    def saveOffsetToZK2(scc: StreamingContext): Unit = {
        val kafkaParams = Map(
            "metadata.broker.list" -> "master:9092,slave1:9092,slave2:9092",
            "zk.list" -> zkQuorum,
            "zk.path" -> "/consumers/directDM/offsets/directDM",
            "group.id" -> "directDM",
            "topic.name" -> "direcrDM"
        )
        val topics = Set("directDM")
        /*查没有过期的用法是什么，而且这个方法好像在新版的Kafka好像也不支持——不是不支持是方法变了*/
        val input = createDirectStream(scc, kafkaParams)
        input.foreachRDD(rdd => {
            rdd.foreach(println _)
            saveOffset(rdd, kafkaParams)
        })
        scc.start()
        scc.awaitTermination()
        scc.stop(false)
    }

    /**
      * 测试一下这个方法
      * 至少有两点信息时可以获取的
      *   1. kafka提供了相应的工具类，针对Direct的消费形式
      *   2. 一个特殊形式的map作为参数即可
      *
      * @param scc
      * @param configs
      * @return
      */

    def createDirectStream(scc: StreamingContext, configs: Map[String, String]): InputDStream[(String, String)] = {
        val fromOffsets = ZKUtils.readOffsets(configs)
        val recordDStream = if (fromOffsets.nonEmpty) {
            KafkaUtils.createDirectStream[String, String, StringDecoder, StringDecoder, (String, String)](scc, configs, fromOffsets, (mmd: MessageAndMetadata[String, String]) => (mmd.key, mmd.message))
        } else {
            val topics = Set(configs("topic.name"))
            KafkaUtils.createDirectStream[String, String, StringDecoder, StringDecoder](scc, configs, topics)
        }
        recordDStream
    }

    /**
      * 保存offset 实现方法是纯粹的借助 ZkClient完成的，也可以理解
      *
      * @param rdd
      * @param zkList
      * @param savePath 参数是参考zookeeper对offset 节点的命名特点  /consumers/消费组/offsets/topic         其中下面还要到分区，但是分区信息是包含在了RDD中的
      */
    def saveOffset(rdd: RDD[(String, String)], parameter: Map[String, String]): Unit = {
        val offsetRanges = rdd.asInstanceOf[HasOffsetRanges].offsetRanges
        println(offsetRanges.mkString("\t"))
        ZKUtils.saveOffsets(parameter.get("zk.list").get, parameter.get("zk.path").get, offsetRanges)
    }


}

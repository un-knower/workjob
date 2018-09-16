package sparkdemo.firstwork

import java.io.{File, FileWriter}
import java.text.SimpleDateFormat
import java.util.{Date, Locale}

import util.{LZUtil, ZKUtils}
import com.alibaba.fastjson.JSON
import kafka.common.TopicAndPartition
import kafka.message.MessageAndMetadata
import kafka.serializer.{Decoder, StringDecoder}
import org.apache.spark.{SparkConf, SparkContext, SparkException}
import org.apache.spark.rdd.RDD
import org.apache.spark.sql.{DataFrame, SparkSession}
import org.apache.spark.storage.StorageLevel
import org.apache.spark.streaming.dstream.{InputDStream, ReceiverInputDStream}
import org.apache.spark.streaming.kafka.{HasOffsetRanges, KafkaCluster, KafkaUtils}
import org.apache.spark.streaming.{Seconds, State, StateSpec, StreamingContext}
import sparkdemo.sparklearn.MysqlUtil


/**
  * 1、 默认是从最新的offset上读取，也就是不发送不读取
  * 2、
  *
  **/
object CombineStreaming {

    case class Bean(category: Int, update_tim: Int, word: String, indexValue: Int, platform_name: String, platform: Int, record_id: String, day: String)

    val sparkSession = SparkSession.builder().appName("test").master("local[1]").getOrCreate()
    val sc = sparkSession.sparkContext
    val tmpdir = "D:/chechpointw"
    val zkQuorum = "localhost:2181"
    val bkQuorum = "localhost:9092"

    import org.apache.spark.streaming.kafka.KafkaUtils

    implicit def String2Int(p: String) = p.toInt

    def main(args: Array[String]): Unit = {
        mapWithStateDemo_interval_update()
    }

    /**
      * 从chekcpoint中恢复计算
      */
    def recover_state_wc(): Unit = {
        val scc = StreamingContext.getOrCreate(tmpdir, createContextFunc)
        scc.start()
        scc.awaitTermination()
    }

    /**
      * 其实最核心的是将业务代码要和创建新流的代码放在一起，否则会出现 流未被初始化的错误
      * 每个程序要有单独的checkpoint目录
      *
      * @return
      */
    def createContextFunc(): StreamingContext = {
        println("新建SparkStreaming 流")
        val sparkConf = new SparkConf().setAppName("SqlNetworkWordCount").setMaster("local[2]")
        sparkConf.set("spark.testing.memory", "2147480000")
        val ssc = new StreamingContext(sparkConf, Seconds(4))
        ssc.checkpoint(tmpdir)
        UpdateStateByKeyDemo(ssc)
        ssc
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
      * 状态维护的操作
      * updateStateByKey可对DStream中的数据按key做reduce，然后对各批次数据累加。
      */
    def UpdateStateByKeyDemo(scc: StreamingContext): Unit = {
        val addFunc = (values: Seq[Int], state: Option[Int]) => {
            val currentCount = values.sum
            val previousCount = state.getOrElse(0)
            Some(currentCount + previousCount)
        }

        val lines: ReceiverInputDStream[String] = scc.socketTextStream("ts", 9999, StorageLevel.MEMORY_AND_DISK_SER)
        val pair = lines.flatMap(_.split(" ")).map(x => (x, 1))
        /*同一个key 的值形成一个序列传了进去*/
        val totalWordCounts = pair.updateStateByKey[Int](addFunc)
        totalWordCounts.print()
        totalWordCounts.checkpoint(Seconds(8))
    }

    /**
      * 与updateStateByKey方法相比，使用mapWithState方法能够得到6倍的低延迟同时维护的key状态数量要多10倍
      */
    def mapWithStateDemo(scc: StreamingContext): Unit = {
        val lines: ReceiverInputDStream[String] = scc.socketTextStream("localhost", 9999, StorageLevel.MEMORY_AND_DISK_SER)
        val word = lines.flatMap(_.split(" ")).map(x => (x, 1)).reduceByKey(_ + _)
        //自定义mappingFunction，累加单词出现的次数并更新状态,其实这个比updateStateByKey更容易理解，参数就是 word ,个数，（word,count）
        val mappingFunc = (word: String, count: Option[Int], state: State[Int]) => {
            // 一般情况下是1  其实可以在上面先完成无状态计算再完成有状态更新,这个时候就不是 1 了 lines.flatMap(_.split(" ")).map(x=>(x,1)).reduceByKey(_+_)
            println(count.getOrElse(0))
            val sum = count.getOrElse(0) + state.getOption.getOrElse(0)
            val output = (word, sum)
            state.update(sum)
            output
        }
        //调用mapWithState进行管理流数据的状态
        val stateDstream = word.mapWithState(StateSpec.function(mappingFunc)).print()
        scc.start()
        scc.awaitTermination()
    }

    /**
      * 每隔多长时间重新维护（丢弃以前的状态）
      * 这里要注意checkpoint的维护问题，就是你在下次正常重启的时候不能使用checkpoint里面的内容，否则不能起到丢弃以前状态重新计算的目的
      * 缺陷就是失败之后无法维护状态
      */
    def mapWithStateDemo_interval(): Unit = {
        while (true) {
            val ssc = new StreamingContext(sc, Seconds(10))
            ssc.checkpoint("D:/checkpoint")
            val lines: ReceiverInputDStream[String] = ssc.socketTextStream("localhost", 9999, StorageLevel.MEMORY_AND_DISK_SER)
            val word = lines.flatMap(_.split(" ")).map(x => (x, 1)).reduceByKey(_ + _)
            //自定义mappingFunction，累加单词出现的次数并更新状态,其实这个比updateStateByKey更容易理解，参数就是 word ,个数，（word,count）
            val mappingFunc = (word: String, count: Option[Int], state: State[Int]) => {
                // 一般情况下是1  其实可以在上面先完成无状态计算再完成有状态更新,这个时候就不是 1 了 lines.flatMap(_.split(" ")).map(x=>(x,1)).reduceByKey(_+_)
                println(count.getOrElse(0))
                val sum = count.getOrElse(0) + state.getOption.getOrElse(0)
                val output = (word, sum)
                state.update(sum)
                output
            }
            //调用mapWithState进行管理流数据的状态
            val stateDstream = word.mapWithState(StateSpec.function(mappingFunc)).print()
            ssc.start()
            /*这里的单位是毫秒  现在才是1 分钟*/
            ssc.awaitTerminationOrTimeout(60 * 1000)
            ssc.stop(false, true)
            println("================================================重启计算中=================================================")
        }
    }

    /**
      * 每隔特定的时间一个checkpoint 如果在该间隔内失败 则使用checkpoint重启，注意停止时间要重新计算
      * 新的问题是：能否在这个时间间隔内完成重启
      *
      */
    def mapWithStateDemo_interval_update(): Unit = {
        val format = new SimpleDateFormat("yyyy-MM-dd")
        while (true) {
            val day = format.format(new Date(System.currentTimeMillis()))
            val dir = "D:/checkpoint" + day
            val ssc = StreamingContext.getOrCreate(dir, getStreamingContext)
            ssc.start()
            /*这里的单位是毫秒  现在才是1 分钟*/
            ssc.awaitTerminationOrTimeout(1000 * 200)
            ssc.stop(false, true)
            println("================================================重启计算中=================================================")
        }
    }

    def getStreamingContext(): StreamingContext = {
        println("===========================新的流被创建===============================")
        val format = new SimpleDateFormat("yyyy-MM-dd")
        val day = format.format(new Date(System.currentTimeMillis()))
        val ssc = new StreamingContext(sc, Seconds(5))
        ssc.checkpoint("D:/checkpoint" + day)
        val lines: ReceiverInputDStream[String] = ssc.socketTextStream("localhost", 9999, StorageLevel.MEMORY_AND_DISK_SER)
        val word = lines.flatMap(_.split(" ")).map(x => (x, 1)).reduceByKey(_ + _)
        //自定义mappingFunction，累加单词出现的次数并更新状态,其实这个比updateStateByKey更容易理解，参数就是 word ,个数，（word,count）
        val mappingFunc = (word: String, count: Option[Int], state: State[Int]) => {
            // 一般情况下是1  其实可以在上面先完成无状态计算再完成有状态更新,这个时候就不是 1 了 lines.flatMap(_.split(" ")).map(x=>(x,1)).reduceByKey(_+_)
            val sum = count.getOrElse(0) + state.getOption.getOrElse(0)
            val output = (word, sum)
            state.update(sum)
            output
        }
        //调用mapWithState进行管理流数据的状态
        val stateDstream = word.mapWithState(StateSpec.function(mappingFunc)).print()
        ssc
    }

    def stopSreamingGracefull(): Unit = {
        val ssc = new StreamingContext(sc, Seconds(10))
        val lines: ReceiverInputDStream[String] = ssc.socketTextStream("localhost", 9999, StorageLevel.MEMORY_AND_DISK_SER)
        val word = lines.flatMap(_.split(" ")).map(x => (x, 1)).reduceByKey(_ + _)
        new Thread(new Runnable() {
            override def run(): Unit = {
                Thread.sleep(3000)
                ssc.stop(false, true)
            }
        }).start()
        ssc.start()
        ssc.awaitTermination()
    }

    def wcStatful(scc: StreamingContext): Unit = {
        val updatefunc = (values: Seq[Int], state: Option[Int]) => {
            val currentCount = values.foldLeft(0)(_ + _)
            val previousCount = state.getOrElse(0)
            Some(currentCount + previousCount)
        }
        val lines = scc.socketTextStream("ts", 9999)
        val words = lines.flatMap(_.split(" ")).map(x => (x, 1)).reduceByKey(_ + _)
        //这一步其实相当于做了优化
        val stateDStream = words.updateStateByKey(updatefunc)
        stateDStream.print()
        scc.start()
        scc.awaitTermination()
    }


    def wc_window(scc: StreamingContext): Unit = {
        val lines: ReceiverInputDStream[String] = scc.socketTextStream("ts", 9999)
        val pair = lines.flatMap(_.split(" ")).map(x => (x, 1))
        val words = pair.reduceByKeyAndWindow((a: Int, b: Int) => (a + b), Seconds(6), Seconds(2))
        words.foreachRDD(_.foreach(println(_)))
        scc.start()
        scc.awaitTermination()

    }

    /**
      * foreachRDD 设计模式的使用
      * 如何正确高效的创建外部链接，对数据进行输出  假设我要将每一条数据输出到文件
      * 错误：因为这需要将连接对象序列化并从 driver 发送到 worker   正确的解决方案是在 worker 创建连接对象.  RDD级别上创建错误  record上创建低效
      * 愿景：创建一个链接池，来更高效的使用
      */
    def foreachRddDesign(scc: StreamingContext): Unit = {
        val input = KafkaUtils.createStream(scc, zkQuorum, "kingcall_Group", Map("longzhuresty" -> 1), StorageLevel.MEMORY_ONLY)
        input.foreachRDD(rdd => {
            rdd.foreachPartition(x => {
                val fileWriter: FileWriter = new FileWriter(new File("C:\\Users\\kingc\\Desktop\\data.txt"), true)
                x.foreach(y => {
                    println(y)
                    fileWriter.write(y.toString() + "\r\n")
                })
                fileWriter.close()
            })
        })
        scc.start()
        scc.awaitTermination()
        scc.stop(false)
    }

    def netWordcount(scc: StreamingContext): Unit = {
        /**
          * lines Dstream
          * 多长时间生成一个 RDD
          */
        val lines = scc.socketTextStream("ts", 9999)
        lines.foreachRDD(
            println(_)
        )
        scc.start()
        scc.awaitTermination()

    }

    def localWorldCount(scc: StreamingContext): Unit = {
        val lines = scc.textFileStream("hdfs://ts:9000/wc")
        val words = lines.flatMap(_.split(" "))
        words.map(x => (x, 1)).reduceByKey(_ + _)
        words.print()
        lines.checkpoint(Seconds(1000))
        scc.start()
        scc.awaitTermination()

    }

    /**
      * 测试一下RDD 和 itrator的关系
      */
    def testRDD_itrator(scc: StreamingContext): Unit = {
        val sc = scc.sparkContext
        val mymap = Map("1" -> "a", "2" -> "b", "3" -> "c", "4" -> "d", "5" -> "e").toArray
        val rdd: RDD[(String, String)] = sc.parallelize(mymap, 1)
        val tmpRdd: RDD[String] = rdd.mapPartitions(convert)
        tmpRdd.foreach(println(_))


    }

    def convert(iterator: Iterator[(String, String)]): Iterator[String] = {
        iterator.map(_._2)
    }

    /**
      * 用sparksession 处理json文件
      */
    def readJson(): Unit = {
        val sparkSession: SparkSession = SparkSession.builder().appName("Base Demo").master("local[4]").getOrCreate()
        val DF = sparkSession.read.json("src/main/resources/externalfile/plu_redis.json")
        DF.show(100, false)
    }


    def RddFilter(): Unit = {
        val sparkSession: SparkSession = SparkSession.builder().master("local[2]").appName("RddFilter").getOrCreate()
        import sparkSession.implicits._
        val rdd = sparkSession.sparkContext.textFile("src/main/resources/kingcall/text.txt").repartition(1)
        val DF = rdd.map(x => {
            val tmpobj = JSON.parseObject(x)
            val category: Int = tmpobj.getIntValue("category")
            val update_time: Int = tmpobj.getIntValue("update_time")
            val word: String = tmpobj.getString("word").trim.replace("[^(a-zA-Z0-9\\u4e00-\\u9fa5)]", "")
            val indexValue: Int = tmpobj.getIntValue("index_value")
            val platform_name: String = tmpobj.getString("platform_name")
            val platform: Int = tmpobj.getIntValue("platform")
            val record_id: String = tmpobj.getString("record_id").trim
            val day: String = tmpobj.getString("day").trim
            Bean(category, update_time, word, indexValue, platform_name, platform, record_id, day)
        }).toDF("category", "update_time", "word", "index_Value", "platform_name", "platform", "record_id", "day")
        DF.createOrReplaceTempView("index_filter_tmp")
        val result: DataFrame = sparkSession.sql(
            s"""
               |select
               |   distinct day,category,update_time,word,index_Value,platform_name,platform,record_id
               |from
               |   index_filter_tmp
        """.stripMargin
        )
        println(DF.count())
        println(result.count())
        result.show(100)
        MysqlUtil.saveToMysql(result, "rddfilter")
    }

}





package util

import java.sql.Timestamp

import com.alibaba.fastjson.JSONObject
import constant.JobConfigDef.JOB_DURATION
import constant.RedisDef
import constant.RedisDef._
import kafka.common.TopicAndPartition
import kafka.message.MessageAndMetadata
import kafka.serializer.StringDecoder
import org.apache.commons.lang3.StringUtils
import org.apache.http.client.methods.HttpPost
import org.apache.http.entity.StringEntity
import org.apache.http.impl.client.HttpClientBuilder
import org.apache.spark.SparkConf
import org.apache.spark.rdd.RDD
import org.apache.spark.sql.SparkSession
import org.apache.spark.streaming.dstream.InputDStream
import org.apache.spark.streaming.{Seconds, StreamingContext}
import org.apache.spark.streaming.kafka.{HasOffsetRanges, KafkaUtils}
import scala.collection.JavaConversions._
import scala.WorkTemplate.StreamJobConfigs
import scala.collection.mutable

/**
  * Created by Andy on 2017/5/12 0012.
  */
trait Job extends Logging {
    val paramsContainer = mutable.Map[String, Any]()
    var configs = mutable.Map[String, String]()

    def initJobConf(conf: String): mutable.Map[String, String] = LZUtil.readFile(conf)

    def saveOffset(rdd: RDD[(String, String)], zkList: String, savePath: String) = {
        val offsetRanges = rdd.asInstanceOf[HasOffsetRanges].offsetRanges
        //    ZKUtils.saveOffsets(zkList, savePath, offsetRanges)
    }

    private def enableRedisSupport(sparkConf: SparkConf, parameters: Map[String, String]) = {
        parameters.get("enable.redis") match {
            case Some(_) =>
                sparkConf.set(REDIS_HOST, parameters(REDIS_HOST))
                        .set(REDIS_PORT, parameters(REDIS_PORT))
            case None => sparkConf
        }
    }

    def getSparkSession(appName: String, parameters: Map[String, String] = Map()): SparkSession = {
        val sparkConf = new SparkConf()
        if (parameters.nonEmpty) {
            parameters.foreach(kv => sparkConf.set(kv._1, kv._2))
        }
        //enable redis
        enableRedisSupport(sparkConf, parameters)
        val sparkSession = SparkSession.builder()
                .config(sparkConf).appName(appName)
        //    logWarning(s"CONFIG DETAIL :\n [ ${parameters.mkString("\n")} ]")
        parameters.get("enable.hive") match {
            case Some(_) =>
                sparkSession.enableHiveSupport().getOrCreate()
            case None => sparkSession.getOrCreate()
        }
    }

    def saveErrorStatus(job: String, msg: String) = {
        val jedis = RedisClient.getWriteClient
        try {
            val time = DateUtils.formatDate(new Timestamp(System.currentTimeMillis()), 0, "yyyyMMddHHmm")
            jedis.hset(JOB_STATUS_ERROR.toString, s"$job:$time", msg)
        } catch {
            case e: Exception =>
                e.printStackTrace()
                logError("=saveErrorStatus= error when saveErrorStatus")
        } finally {
            jedis.close()
        }
    }

    /**
      * 实时任务创建流
      */
    def createDirectStream(ssc: StreamingContext, streamConfigs: Map[String, String]) = {
        log.warn("kafka的相关配置如下:" + "\n" + streamConfigs.mkString("\n"))
        // kafka相关配置
        val kafkaParams = streamConfigs.filterNot { case (k, _) => k.startsWith("zk") || k.startsWith("topic") }
        val km = new KafkaManager(kafkaParams)
        var topics = scala.collection.mutable.Set[String]()
        streamConfigs("topic.name").split(",").foreach(ele => {
            topics += ele
        })
        log.warn("主题:" + topics)
        val recordDStream = km.createDirectStream[String, String, StringDecoder, StringDecoder](ssc, kafkaParams, topics.toSet[String])
        (recordDStream, km)
    }

    /**
      * 这里启动流处理程序
      *
      * @param f 处理程序的核心逻辑
      */
    def startStreaming(f: (RDD[(String, String)]) => Unit): Unit = {
        val ss = paramsContainer("sparkSession").asInstanceOf[SparkSession]
        val sc = ss.sparkContext
        val stc: StreamingContext = new StreamingContext(sc, Seconds(configs(JOB_DURATION).toInt))
        val result = createDirectStream(stc, configs.filter(_._1.startsWith("stream")).map { case (k, v) => k.replace("stream.", "") -> v }.toMap)
        val recordDStream: InputDStream[(String, String)] = result._1
        val km: KafkaManager = result._2
        recordDStream.foreachRDD(rdd => {
            if (rdd.count() > 0) {
                f(rdd)
                km.updateZKOffsets(rdd)
            }
        })
        stc.start()
        stc.awaitTermination()
    }

    /**
      * 这里启动流处理程序
      *
      * @param f 处理程序的核心逻辑
      */
    def startStreaming_stream(f: (InputDStream[(String, String)]) => Unit): Unit = {
        val ss = paramsContainer("sparkSession").asInstanceOf[SparkSession]
        val sc = ss.sparkContext
        val stc: StreamingContext = new StreamingContext(sc, Seconds(configs(JOB_DURATION).toInt))
        val result = createDirectStream(stc, configs.filter(_._1.startsWith("stream")).map { case (k, v) => k.replace("stream.", "") -> v }.toMap)
        val recordDStream: InputDStream[(String, String)] = result._1
        val km: KafkaManager = result._2
        /*觉得这样有问题，但是能运行,发现如果如果 f 函数中*/
        recordDStream.foreachRDD(rdd => {
            km.updateZKOffsets(rdd);
            println("测试在维护offset中")
        })
        f(recordDStream)
        stc.start()
        stc.awaitTermination()
    }

    /**
      * 在这里看到了tranform的强大之处
      * 这里启动流处理程序
      *
      * @param f 处理程序的核心逻辑
      */
    def startStreaming_stream2(f: (InputDStream[(String, String)]) => Unit): Unit = {
        val ss = paramsContainer("sparkSession").asInstanceOf[SparkSession]
        val sc = ss.sparkContext
        val stc: StreamingContext = new StreamingContext(sc, Seconds(configs(JOB_DURATION).toInt))
        val result = createDirectStream(stc, configs.filter(_._1.startsWith("stream")).map { case (k, v) => k.replace("stream.", "") -> v }.toMap)
        val recordDStream: InputDStream[(String, String)] = result._1
        val km: KafkaManager = result._2
        /*但是这样维护的offset 已经么没有任何意义了,但是提供了一种思路*/
        recordDStream.transform { rdd => {
            km.updateZKOffsets(rdd);
            rdd
        }
        }
        stc.start()
        stc.awaitTermination()
    }

    /**
      * 创建多个topic的streaming
      */
    def createStreaming(ssc: StreamingContext, streamConfigs: Map[String, String]) = {
        // kafka相关配置
        val kafkaParams = streamConfigs.filterNot { case (k, _) => k.startsWith("zk") || k.startsWith("topic") }
        logWarning(s"kafka configs detail : \n ${kafkaParams.mkString("\n")}")
        val fromOffsets = readOffsetFromRedis(streamConfigs, StreamJobConfigs.JOB_NAME)
        fromOffsets.foreach(offsetRange => logWarning(s"Using $offsetRange"))
        KafkaUtils.createDirectStream[String, String, StringDecoder, StringDecoder, (String, String)](ssc, kafkaParams, fromOffsets, (mmd: MessageAndMetadata[String, String]) => (mmd.topic, mmd.message))
    }

    def getCurrentTs: Long = System.currentTimeMillis() / 1000

    /**
      * 上报数据到运维统计
      *
      * @param params
      * @param url
      */
    def reportData(params: Map[String, String], url: String = "http://192.168.9.55:9093/add") = {
        try {
            new Runnable {
                override def run() = {
                    val post = new HttpPost(url)
                    post.setHeader("Content-Type", "application/json")
                    val json = new JSONObject()
                    params.foreach { case (k, v) => json.put(k, v) }
                    post.setEntity(new StringEntity(json.toString, "utf-8"))
                    val httpClient = HttpClientBuilder.create.build
                    httpClient.execute(post)
                }
            }.run()
        } catch {
            case e: Exception =>
                logError(s"report data cause error with params :\n ${params.mkString("\n")} ")
                e.printStackTrace()
        }
    }

    def readOffsetFromRedis(configs: Map[String, String], key: String): Map[TopicAndPartition, Long] = {
        implicit val map = configs
        val read = RedisClient.getReadClient
        val offsetJson = read.hget(RedisDef.STREAMING_OFFSET, key)
        read.close()
        if (StringUtils.isNotBlank(offsetJson)) {
            logWarning(s"read offset from redis : $offsetJson")
            val tp = JsonUtils.readJsonToMap(offsetJson).flatMap { case (t, v) =>
                s"$v".split(",").map(_.split(":")).map { case Array(p, o) =>
                    TopicAndPartition(s"$t", p.toInt) -> o.toLong
                }
            }.toMap[TopicAndPartition, Long]
            checkOffsets(tp)
        } else {
            logWarning("using latest offset")
            val topics = configs("topic.name").split(",").toList
            val brokerList = configs("metadata.broker.list")
            val groupId = configs("group.id")
            val lastMap = KafkaOffsetTool.getInstance.getLastOffset(brokerList, topics, groupId)
            lastMap.map { case (t, p) => t -> p.toLong }.toMap
        }
    }

    def checkOffsets(fromOffset: Map[TopicAndPartition, Long])(implicit configs: Map[String, String]): Map[TopicAndPartition, Long] = {
        val topics = configs("topic.name").split(",").toList
        val brokerList = configs("metadata.broker.list")
        val groupId = configs("group.id")
        // lastest offsets
        val lastMap = KafkaOffsetTool.getInstance.getLastOffset(brokerList, topics, groupId)
        // earliest offsets
        val earlierMap = KafkaOffsetTool.getInstance.getEarliestOffset(brokerList, topics, groupId)
        logWarning(s"last offset map :$lastMap ,earlier Map :$earlierMap")
        fromOffset.map {
            case (t, offset) =>
                val lastOffset: Long = lastMap.get(t)
                val earliestOffset: Long = earlierMap.get(t)
                logWarning(s"last offset : $lastOffset , earliest Offset :$earliestOffset")
                if (offset > lastOffset || offset < earliestOffset) {
                    (t, earliestOffset)
                } else {
                    (t, offset)
                }
        }
    }

    /** *
      * save offset as json format to redis
      *
      * @param rdd
      */
    def saveOffsetAsJson[T](rdd: RDD[T]) = {
        val write = RedisClient.getWriteClient
        try {
            val offsetRanges = rdd.asInstanceOf[HasOffsetRanges].offsetRanges
            val m = offsetRanges.map(offsetRange => offsetRange.topic -> s"${offsetRange.partition}:${offsetRange.fromOffset}")
                    .groupBy(_._1).map(x => x._1 -> x._2.map(_._2).mkString(","))
            val json = JsonUtils.writeMapToJson(m)
            write.hset(RedisDef.STREAMING_OFFSET, StreamJobConfigs.JOB_NAME, json)
        } catch {
            case e: Exception =>
                WechatUtils.sendWechatMsg("Job:saveOffsetAsJson", e.getLocalizedMessage)
                logError("=saveOffsetAsJson= error when save offset to redis")
        } finally {
            write.close()
        }
    }

    def generatePartition(): (String, String, String) = {
        val day = DateUtils.getCurrentDate
        val year = DateUtils.formatDateTime(day, "yyyy")
        val month = DateUtils.formatDateTime(day, "yyyy-MM")
        (year, month, day)
    }

    def cleanSymbols(par: Iterator[(String, String)]): Iterator[String] =
        par.map { case (_, j) => j.replaceAll("\\\r|\\\n|\\r|\\n|\\\\r|\\\\n|\r|\n|\\t|\\|", "") }.filter(!_.isEmpty)

    def clearSymbols(par: Iterator[(String, String)]): Iterator[(String, String)] =
        par.map { case (i, j) => i -> j.replaceAll("\\\r|\\\n|\\r|\\n|\\\\r|\\\\n|\r|\n|\\t|\\|", "") }.filter(x => x._2.nonEmpty)
}

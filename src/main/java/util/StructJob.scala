package util

import org.apache.spark.SparkConf
import org.apache.spark.sql.functions.from_json
import org.apache.spark.sql.streaming.StreamingQuery
import org.apache.spark.sql.types.{StringType, StructField, StructType}
import org.apache.spark.sql.{DataFrame, Dataset, SparkSession}

import scala.collection.mutable

/**
  * 后补 offset维护
  */
trait StructJob extends Logging {
    val paramsContainer = mutable.Map[String, Any]()
    var configs = mutable.Map[String, String]()

    def initJobConf(conf: String): Unit = {
        configs = LZUtil.readFile(conf)
    }


    def getSparkSession(appName: String, parameters: Map[String, String] = Map()): SparkSession = {
        val sparkConf = new SparkConf()
        if (parameters.nonEmpty) {
            parameters.foreach(kv => sparkConf.set(kv._1, kv._2))
        }
        val tmpsession = SparkSession.builder()
                .config(sparkConf).appName(appName)
        parameters.get("enable.hive") match {
            case Some(_) =>
                tmpsession.enableHiveSupport().getOrCreate()
            case None => tmpsession.getOrCreate()
        }
    }


    /**
      * 实时任务创建流,这里没有offset的维护，以后应该补上
      */
    def createStructStream(): DataFrame = {
        /*下面这个写法是固定的*/
        val sparkSession = paramsContainer("sparkSession").asInstanceOf[SparkSession]
        import sparkSession.implicits._
        val df = sparkSession
                .readStream
                .format("kafka")
                .option("kafka.bootstrap.servers", configs("kafka.broker.list"))
                .option("subscribe", configs("kafka.topics.list"))
                .option("group.id", configs("kafka.group"))
                .load()
        val DF=df.selectExpr("CAST(value AS STRING)").as[String].toDF("value")
        DF.select(from_json($"value", configs("fields")) as "value").select($"value.*")
    }

    /**
      * 这里启动流处理程序
      *
      * @param f 处理程序的核心逻辑
      */
    def startStreaming(f: (DataFrame) => StreamingQuery): Unit = {
        val stream = createStructStream()
        f(stream)
        val spark = paramsContainer("sparkSession").asInstanceOf[SparkSession]
        spark.streams.awaitAnyTermination()
    }

    //隐式转换函数:定义表的schema
    implicit def defineSchema(fieldsStr: String): StructType = {
        val fieldsList = fieldsStr.split(",").toList
        StructType(fieldsList.map(a => StructField(a, StringType, nullable = true)))
    }

}

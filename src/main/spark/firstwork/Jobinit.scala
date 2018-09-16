package sparkdemo.firstwork

import org.apache.spark.SparkConf
import org.apache.spark.sql.SparkSession

import scala.collection.mutable
import scala.io.Source

trait Jobinit {
    val paramsContainer = mutable.Map[String, Any]()
    var configs = mutable.Map[String, String]()

    /**
      * 初始化job 所需的参数  也就是读取配置文件
      *
      * @param fileName
      * @return
      */
    def initJobConf(fileName: String): mutable.Map[String, String] = {
        val prop = mutable.Map[String, String]()
        val kv = (s: String) => {
            val idx = s.indexOf("=")
            prop.+=(s.substring(0, idx) -> s.substring(idx + 1, s.length))
        }
        Source.fromInputStream(
            this.getClass.getClassLoader.getResource(fileName).openStream()
        ).getLines().filterNot(_.startsWith("#")).foreach(kv)
        prop
    }

    /**
      * 根据配置参数获取 SparkSession 对象
      *
      * @param appName
      * @param parameters
      * @return
      */
    def getSparkSession(appName: String, parameters: Map[String, String] = Map()): SparkSession = {
        val sparkConf = new SparkConf()
        if (parameters.nonEmpty) {
            parameters.foreach(kv => sparkConf.set(kv._1, kv._2))
        }
        //enableRedisSupport(sparkConf, parameters)
        val sparkSession = SparkSession.builder()
                .config(sparkConf).appName(appName)
        parameters.get("enable.hive") match {
            case Some(_) =>
                sparkSession.enableHiveSupport().getOrCreate()
            case None => sparkSession.getOrCreate()
        }
    }

}

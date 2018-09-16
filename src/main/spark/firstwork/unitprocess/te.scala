package sparkdemo.firstwork.unitprocess

import com.alibaba.fastjson.JSON
import org.apache.spark.rdd.RDD
import org.apache.spark.sql.types.{StringType, StructField, StructType}
import org.apache.spark.sql.{DataFrame, Row, SparkSession}

import scala.collection.mutable.{ArrayBuffer, ListBuffer}

object te {
    val sparkSession: SparkSession = SparkSession.builder().appName("Base Demo").master("local[4]")
            .getOrCreate()

    /**
      * 问题是数据一旦读成 DF 之后就无法反转成  json
      *
      * @param args
      */
    def main(args: Array[String]): Unit = {
        deal2()
    }

    /*
    * 目前的发现是直接存储为json 是可以的
    * */
    def deal2(): Unit = {
        val fields = "_v,aid,an,av,cd,cds,chn,cid,city,cms,country,cts,dmm,dmt,dmv,dpv,ds,ea,ec,el,ev,flt,guid,isp,lat,mt,ns_ts,province,pt,s_hn,s_ts,s_uip,sbt,sid,so,sr,t,tid,ua,uid,ul,uno,unt,uuid,v"
        val tmpsql = "select @(s_ts,'yyyy') as year ,@(s_ts,'yyyy-MM') as month,@(s_ts,'yyyy-MM-dd') as day,%s from huludetail"
        val sql = tmpsql.replace("@", "from_unixtime").format(fields)
        println(sql)
        val jsonrdd: RDD[String] = sparkSession.sparkContext.textFile("src/main/resources/webandapp/app_log_event_v2.txt")
        val read = sparkSession.read
        // 这个做法可以使原来在DF 中不存在的字段都可以运行,而且避免了可能因为数据类型转换带来的错误
        read.schema(fields)
        read.json(jsonrdd).createOrReplaceTempView("huludetail")
        val DF = sparkSession.sql(sql)
        DF.show()
    }

    implicit def defineSchema(fieldsStr: String): StructType = {
        println("隐式函数被调用:")
        val fieldsList = fieldsStr.split(",").toList
        StructType(fieldsList.map(a => StructField(a, StringType, nullable = true)))
    }

    def saveStreamingAsOrc(df: DataFrame,
                           path: String,
                           partitionBy: Array[String],
                           saveMode: String = "append"): Unit = {
        df
                .write.mode(saveMode)
                .partitionBy(partitionBy: _*)
                .orc(path)
    }

    def saveAsStreamingText(df: DataFrame, path: String): Unit = {
        df.repartition(1)
                .write.mode("append")
                .partitionBy("year", "month", "day")
                .option("delimiter", "|")
                .text(path)
    }

    /*
    *  将 DF 存储为 json 但是没有分区了,而且这个存储路径期初是不能存在
    * */
    def saveAsStreamingJsonText(df: DataFrame, path: String): Unit = {
        df.write.json(path)
    }

    /**
      *
      * @param iterator
      * @param fieldsList
      * @return 前三个是分区，最后一个将 row 中的值进行了组合，形成了一个字符串
      */
    def textFormat(iterator: Iterator[Row], fieldsList: List[String]): Iterator[(String, String, String, String)] = {
        val res = ArrayBuffer.empty[(String, String, String, String)]
        iterator.foreach(row => {
            val records = ListBuffer[String]()
            val valueMap = row.getValuesMap(fieldsList)
            fieldsList.foreach { field =>
                try {
                    val value = valueMap.getOrElse(field, "null")
                    value match {
                        case "" => records.+=(null)
                        case _ => records.+=(value)
                    }
                } catch {
                    case e: Exception =>
                        e.printStackTrace()
                        println("=========================================================")
                        println(field)
                }
            }
            res.+=((row.getString(0), row.getString(1), row.getString(2), records.mkString("|")))
        })
        res.iterator
    }
}

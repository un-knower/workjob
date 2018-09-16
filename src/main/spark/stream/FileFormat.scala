package stream

import java.io.StringReader

import au.com.bytecode.opencsv.CSVReader
import org.apache.spark.SparkConf
import org.apache.spark.rdd.RDD
import org.apache.spark.streaming.twitter._
import org.apache.spark.streaming.{Seconds, StreamingContext}
import org.codehaus.jackson.map.ObjectMapper
import stream.util.{MDBManager, Text}

/**
  * 文件格式
  */
object FileFormat {
    val conf = new SparkConf().setAppName("stream learn").setMaster("local[2]")
    val ssc = new StreamingContext(conf, Seconds(2))
    val sc = ssc.sparkContext

    def main(args: Array[String]): Unit = {
        twe()
    }

    /**
      * textFile 也可以读取文件夹，但是会将内容混合在一起，不知道哪些拿龙来自哪里
      */
    def textFileOp(): Unit ={
        val read=sc.textFile("chenm/partition",10)
        read.foreach(println(_))
    }

    /**
      * 读取一个文件夹下的所有文本文件。注意其他格式的文件不会读的，会返回paitRdd key 是文件名 value 是文件内容
      * 支持分区数目的控制
      */
    def wholeTextFilesOp(): Unit ={
        val read:RDD[(String,String)]=sc.wholeTextFiles("chenm/partition",10)
        read.foreach(println(_))
    }

    /**
      * 处理json
      */
    def json_op(): Unit ={
        val input = sc.textFile("chenm/text.txt")
        val result=input.map {
            record => {
                val mapper = new ObjectMapper()
                try {
                    Some(mapper.readValue(record, classOf[Text]))
                } catch {
                    case e: Exception => println(e)
                }
            }
        }
        result.foreach(println(_))
    }
    /**
      * 结果有点意料之外
      */
    def csv_op(): Unit = {
        val input = sc.textFile("doc/sample_map.csv")
        val result6 = input.map { line =>
            val reader = new CSVReader(new StringReader(line));
            reader.readNext();
        }
        for (result <- result6) {
            for (re <- result) {
                println(re)
            }
        }
    }

    /**
      * 使用c3p0 的数据库连接池
      */
    def use_C3P0(): Unit ={
        // 数据是每个页面的页面名称、uv、pv
        val data=sc.parallelize(Array(("a",2,3),("b",3,4),("c",4,3)),3)
        data.foreachPartition(data=>{
            //从连接池中获取一个连接
            val conn = MDBManager.getMDBManager(true).getConnection
            conn.setAutoCommit(false)
            val sql = "insert into tableName set pageName=?,uvNum=?,pvNum=?"
            val preparedStatement = conn.prepareStatement(sql)
            data.foreach(r => {
                preparedStatement.setObject(1, r._1)
                preparedStatement.setObject(2, r._2)
                preparedStatement.setObject(3, r._3)
                preparedStatement.addBatch()
            })
            //批量提交，如果数据量大，这里可以分批提交
            preparedStatement.executeBatch()
            conn.commit()
            conn.close()
        })
    }

    def twe(): Unit ={
        val stream = TwitterUtils.createStream(ssc, None)
        val hashTags = stream.flatMap(status => status.getText.split(" ").filter(_.startsWith("#")))
        val topCounts60 = hashTags.map((_, 1)).reduceByKeyAndWindow(_ + _, Seconds(60))
                .map{case (topic, count) => (count, topic)}
                .transform(_.sortByKey(false))

        // Print popular hashtags
        topCounts60.foreachRDD(rdd => {
            val topList = rdd.take(10)
            println("\nPopular topics in last 60 seconds (%s total):".format(rdd.count()))
            topList.foreach{case (count, tag) => println("%s (%s tweets)".format(tag, count))}
        })

        ssc.start()
        ssc.awaitTermination()
    }


}

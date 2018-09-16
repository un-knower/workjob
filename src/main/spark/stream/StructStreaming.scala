package sparkdemo.streaming

/*为什么这玩意维护了状态,不是一个batch算一次  其实并不是维护而是它的特性所致的*/

import java.sql.Timestamp

import com.alibaba.fastjson.JSON
import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.databind.ObjectMapper
import org.apache.spark.sql.functions._
import org.apache.spark.sql.streaming.StreamingQueryListener.{QueryProgressEvent, QueryStartedEvent, QueryTerminatedEvent}
import org.apache.spark.sql.streaming.{StreamingQueryListener, Trigger}
import org.apache.spark.sql.{DataFrame, Dataset, SparkSession}
import org.apache.spark.storage.StorageLevel
import org.apache.spark.streaming.{Seconds, StreamingContext}

import scala.collection.JavaConversions._

/*
* 没有自定义操作这一块 源码中有一个例子
* 流管理器是管理众多流的
* 流管理是通过流对象本身管理的
* 关于json的解析，不能使用sparksession读取的方式来解析，要借助专门的的解析函数
* */
@JsonIgnoreProperties(ignoreUnknown = true)
case class Bean(client_ip: String, is_blocked: String, args: String, status: String, uid: String, host: String, request_timestamp: Timestamp, testnum: Int) extends Serializable
object StructStreaming {
    implicit val formats = org.json4s.DefaultFormats
    val spark = SparkSession
            .builder
            .appName("StructuredNetworkWordCount").master("local[2]")
            .getOrCreate()

    import spark.implicits._


    def main(args: Array[String]): Unit = {
        join_op_strtem_static()
    }

    def wc_socket(): Unit = {
        val ssc = new StreamingContext(spark.sparkContext, Seconds(2))
        val lines = ssc.socketTextStream("localhost", 9999, StorageLevel.MEMORY_AND_DISK_SER)
        val pair = lines.flatMap(_.split(" ")).map(x => (x, 1)).reduceByKey(_ + _)
        pair.print()
        ssc.start()
        ssc.awaitTermination()
    }

    def wc_struct_socket(): Unit = {
        //line 就是DF     lines is input table  wordCounts is result table
        val lines = spark.readStream
                .format("socket")
                .option("host", "localhost")
                .option("port", 9999)
                .load()
        lines.printSchema()
        // Returns True for DataFrames that have stream sources
        println(lines.isStreaming)
        // Split the lines into words 先转换成了DS
        val words = lines.as[String].flatMap(_.split(" "))

        // Generate running word count
        val wordCounts = words.groupBy("value").count()
        val query = wordCounts.writeStream
                .outputMode("append")
                .format("console")
                .start()
        query.awaitTermination()
    }

    /*延时三种输出模型*/
    def show_three_mode(): Unit = {
        val lines = spark.readStream
                .format("socket")
                .option("host", "localhost")
                .option("port", 9999)
                .load()
        val words = lines.as[String].flatMap(_.split(" "))
        val query = words.writeStream
                .outputMode("append")
                .format("console")
                .start()
        query.awaitTermination()

    }


    def wc_window_struct_socket(): Unit = {
        //line 就是DF     lines is input table  wordCounts is result table
        val lines = spark.readStream
                .format("socket")
                .option("host", "localhost")
                .option("port", 9999)
                .load()
        lines.printSchema()
        // Returns True for DataFrames that have stream sources
        println(lines.isStreaming)
        // Split the lines into words 先转换成了DS
        val words = lines.as[String].flatMap(_.split(" "))

        // Generate running word count
        val wordCounts = words.groupBy("value").count()
        val query = wordCounts.writeStream
                .outputMode("complete")
                .format("console")
                .start()
        query.awaitTermination()
    }

    /*演示从kafka读取数据            这个的offset维护(默认的和自动的)*/
    def createSttuctStream_kafka(): Unit = {
        val tmpdf = get_kafka_stream()
        // DF 转 DS val tmpds:Dataset[Bean]=tmpdf.as[Bean]

        /*  借助 DS 完成
            val result1=tmpdf.groupBy("host").count()
            val query1=result1.writeStream.outputMode("complete").format("console").start()
            query1.awaitTermination()*/

        tmpdf.createOrReplaceTempView("test")
        val result = spark.sql("select host ,count(*) as  from test group by host")
        val query = result.writeStream.outputMode("complete").format("console").start()
        query.awaitTermination()
    }


    def get_kafka_stream(): DataFrame = {
        val df = spark
                .readStream
                .format("kafka")
                .option("kafka.bootstrap.servers", "localhost:9092")
                .option("subscribe", "king")
                .load()
        import spark.implicits._
        val tmpdf = df.selectExpr("CAST(value AS STRING)").as[String].map(line => {
            val tmp=JSON.parseObject(line)
            Bean(tmp.getString("client_ip"), tmp.getString("is_blocked"), tmp.getString("args"), tmp.getString("status"), tmp.getString("uid"), tmp.getString("host"),
                new Timestamp(tmp.getString("request_timestamp").toLong), tmp.getInteger("testnum"))
        }).toDF()
        tmpdf
    }

    /*关于kafka的操作*/
    def kafka_op(): Unit = {
        val df = spark
                .readStream
                .format("kafka")
                .option("kafka.bootstrap.servers", "localhost:9092")
                .option("subscribe", "app_log_screenview2")
                .load()
        val dataset: Dataset[String] = df.selectExpr("CAST(value AS STRING)").as[String] map (line => {
            val set = scala.collection.mutable.Set[String]()
            val obj = JSON.parseObject(line)
            for (key <- obj.keySet()) {
                set.add(obj.getString(key))
            }
            set.mkString("=")
        })
        val query = dataset.writeStream
                .format("console")
                //      .option("path","D:/test")
                //      .option("checkpointLocation","D:/checkpoint")
                .outputMode("append").start()
        query.awaitTermination()
    }


    /*创建带有监听器的流 通过流对象本身完成对流信息的输出*/
    def get_kafka_stream_with_listener(): DataFrame = {
        spark.streams.addListener(new StreamingQueryListener() {
            override def onQueryStarted(queryStarted: QueryStartedEvent): Unit = {
                println("Query started: " + queryStarted.id)
            }

            override def onQueryTerminated(queryTerminated: QueryTerminatedEvent): Unit = {
                println("Query terminated: " + queryTerminated.id)
            }

            override def onQueryProgress(queryProgress: QueryProgressEvent): Unit = {
                println("Query made progress: " + queryProgress.progress)
            }
        })
        val df = spark
                .readStream
                .format("kafka")
                .option("kafka.bootstrap.servers", "localhost:9092")
                .option("subscribe", "king")
                .load()
        val tmpdf = df.selectExpr("CAST(value AS STRING)").as[String].map(line => {
            val obj = JSON.parseObject(line)
            Bean(obj.getString("client_ip"), obj.getString("is_blocked"), obj.getString("args"), obj.getString("status"), obj.getString("uid"), obj.getString("host"),
                new Timestamp(obj.getString("request_timestamp").toLong), obj.getInteger("testnum"))
        }).toDF()
        tmpdf
    }

    /*这个操作在目前的spark 版本中不支持2.3 才可以,而且这个还缺两个东西*/
    def join_op_strtem_stream(): Unit = {
        val tmpdf = get_kafka_stream()
        tmpdf.createOrReplaceTempView("test")
        val resultcnt = spark.sql("select host ,count(*) as cnt  from test group by host").createOrReplaceTempView("resultcnt")
        val resultavg = spark.sql("select host ,avg(testnum) as av from test group by host").createOrReplaceTempView("resultavg")
        val result = spark.sql("select a.host,cnt,av from resultcnt c inner join resultavg a on c.host=a.host")
        val query = result.writeStream.outputMode("complete").format("console").start()
        query.awaitTermination()
    }

    /*stream-static 的join 操作是支持的*/
    def join_op_strtem_static(): Unit = {
        val tmpdf = get_kafka_stream()
        tmpdf.createOrReplaceTempView("test")
        val resultcnt = spark.sql("select host ,count(*) as cnt  from test group by host").createOrReplaceTempView("resultcnt")
        spark.read.json("src/main/resources/static.json").createOrReplaceTempView("resulttmp")
        val resultavg = spark.sql("select host,avg(testnum) as av from resulttmp group by host").createOrReplaceTempView("resultavg")
        val result = spark.sql("select a.host,cnt,av from resultcnt c inner join resultavg a on c.host=a.host")
        val query = result.writeStream.outputMode("complete").format("console").start()
        query.awaitTermination()
    }

    /* 不知道为什么没有结果 */
    def window_op(): Unit = {
        val df = get_kafka_stream()
        val result = df.groupBy(window($"request_timestamp", "120 seconds", "60 seconds"), $"host").count()
        val query = result.writeStream.outputMode("update").format("console").start()
        query.awaitTermination()
    }

    def window_op_watermark(): Unit = {
        val df = get_kafka_stream()
        val result = df.withWatermark("request_timestamp", "10 seconds").groupBy(window($"request_timestamp", "120 seconds", "60 seconds"), $"host").count()
        val query = result.writeStream.outputMode("update").format("console").start()
        query.awaitTermination()
    }

    def deduplication_op_watermark(): Unit = {
        val df = get_kafka_stream()
        /*从我数据源的特点上 request_timestamp 是不可能重复的，但是为什么去重的函数中没有 request_timestamp 就不行呢 没有watermark的就可以*/
        val result = df.withWatermark("request_timestamp", "1 seconds").dropDuplicates("host", "request_timestamp")
        val query = result.writeStream.outputMode("update").format("console").start()
        query.awaitTermination()
    }

    /*现象就是你永远看不到数据了，因为重复了*/
    def deduplication_op(): Unit = {
        val df = get_kafka_stream()
        val result = df.dropDuplicates("host")
        val query = result.writeStream.outputMode("append").format("console").start()
        query.awaitTermination()
    }

    /*可以输出到任何地方 包括kafka 和内存 offset 是可以维护在checkpoint 里面的*/
    def sink_op(): Unit = {
        val df = get_kafka_stream()
        val query = df.writeStream.outputMode("append").format("csv").option("checkpointLocation", "D:/checkpoint/").option("path", "D:/test").start()
        query.awaitTermination()
    }

    /*这个有点不好理解   指定的是它的处理时长*/
    def trigger_op(): Unit = {
        val df = get_kafka_stream()
        val query = df.writeStream.outputMode("update").format("console").trigger(Trigger.ProcessingTime("10 second")).start()
        query.awaitTermination()
    }

    /*同时启动多个流   通过 StreamingQueryManager 来管理*/
    def start_tow_or_more_query(): Unit = {
        val tmpdf = get_kafka_stream()
        tmpdf.createOrReplaceTempView("test")
        val resultcnt = spark.sql("select host ,count(*) as cnt  from test group by host")
        val resultavg = spark.sql("select host ,avg(testnum) as av from test group by host")
        val query1 = resultcnt.writeStream.outputMode("update").format("console").start()
        val query2 = resultavg.writeStream.outputMode("update").format("console").start()
        spark.streams.awaitAnyTermination()
    }

    /*管理流 主要通过流对象和流管理器对流进行管理*/
    def manage_query(): Unit = {

    }

    /*监控流*/
    def monitor_query_listener(): Unit = {
        val tmpdf = get_kafka_stream_with_listener()
        tmpdf.createOrReplaceTempView("test")
        val resultcnt = spark.sql("select host ,count(*) as cnt  from test group by host")
        val resultavg = spark.sql("select host ,avg(testnum) as av from test group by host")
        val query1 = resultcnt.writeStream.outputMode("update").format("console").queryName("计数流").start()
        val query2 = resultavg.writeStream.outputMode("update").format("console").queryName("均值流").start()
        spark.streams.awaitAnyTermination()
    }

    /*监控流*/
    def monitor_query_Dropwizard(): Unit = {
        val tmpdf = get_kafka_stream_with_listener()
        tmpdf.createOrReplaceTempView("test")
        val resultcnt = spark.sql("select host ,count(*) as cnt  from test group by host")
        val resultavg = spark.sql("select host ,avg(testnum) as av from test group by host")
        val query1 = resultcnt.writeStream.outputMode("update").format("console").queryName("计数流").start()
        val query2 = resultavg.writeStream.outputMode("update").format("console").queryName("均值流").start()
        spark.streams.awaitAnyTermination()
    }


}

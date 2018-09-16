package settings

import org.apache.hadoop.hdfs.server.common.Storage
import org.apache.spark.storage.StorageLevel
import org.apache.spark.{SparkConf, SparkContext}
import org.apache.spark.streaming.{Seconds, StreamingContext}

/**
  * spark.io.compression.codec
  *     默认值：snappy, 压缩诸如RDD分区、广播变量、shuffle输出等内部数据的编码解码器。默认情况下，Spark提供了三种选择：lz4, lzf和snappy
  *     org.apache.spark.io.LZ4CompressionCodec，org.apache.spark.io.LZFCompressionCodec，org.apache.spark.io.SnappyCompressionCodec
  * spark.io.compression.lz4.block.size 默认值：32768
  * spark.io.compression.snappy.block.size 默认值：32768
  */
object Compress {
    val conf = new SparkConf().setAppName("stream learn").setMaster("local[2]")

    def main(args: Array[String]): Unit = {
        compressRdd
    }


    /**
      *  spark.rdd.compress
      *  这个参数决定了RDD Cache的过程中，RDD数据在序列化之后是否进一步进行压缩再储存到内存或磁盘上
      */
    def compressRdd(): Unit ={
        conf.set("spark.io.compression.codec","org.apache.spark.io.SnappyCompressionCodec")
        conf.set("spark.rdd.compress","true")
        val sc = new SparkContext(conf)
        val rdd=sc.parallelize(Array("a","b","c","d","e","f","g"),2)
        rdd.persist(StorageLevel.MEMORY_ONLY_SER)
        println(rdd.count)
        println(rdd.filter(_>"d").count())


    }

    /**
      * 默认值：true。 在发送广播变量之前是否压缩它。
      */
    def compressBroadCast(): Unit ={

    }

}

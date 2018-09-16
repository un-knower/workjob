package sparkdemo.firstwork.hbasepack

import org.apache.hadoop.conf.Configuration
import org.apache.hadoop.hbase.client.Scan
import org.apache.hadoop.hbase.mapreduce.TableInputFormat
import org.apache.hadoop.hbase.protobuf.ProtobufUtil
import org.apache.hadoop.hbase.util.{Base64, Bytes}
import org.apache.hadoop.hbase.{CellUtil, HBaseConfiguration, TableName}
import org.apache.spark.sql.SparkSession

import scala.collection.JavaConversions._

/**
  * spark 读取Hbase 中的数据
  */
object ReadHbase {
    val conf: Configuration = HBaseConfiguration.create()
    /**
      * 在这里完全可以看出访问 hbase 只需要zookeeper
      */
    conf.addResource("habse.xml")
    conf.set(TableInputFormat.INPUT_TABLE, "spark")
    val sparksession = SparkSession.builder().master("local[2]").getOrCreate()
    val sc = sparksession.sparkContext

    def main(args: Array[String]): Unit = {
        read2()
    }

    def read1(): Unit = {
        val hbaseRdd = sc.newAPIHadoopRDD(conf, classOf[TableInputFormat], classOf[org.apache.hadoop.hbase.io.ImmutableBytesWritable], classOf[org.apache.hadoop.hbase.client.Result])
        println(hbaseRdd.count())
        // 下面就是拿到了result 对象，接下来对result 对象直接操作就可以了
        hbaseRdd.foreach({ case (_, result) =>
            val cells = result.listCells()
            println(cells.size())
            for (cell <- cells) {
                println(Bytes.toString(CellUtil.cloneQualifier(cell)), Bytes.toString(CellUtil.cloneValue(cell)))
            }
        })
        sc.stop()
    }

    /**
      * 带过滤器的读取方式,并且指定了起始行
      */
    def read2(): Unit = {
        var scan = new Scan();
        scan.addColumn(Bytes.toBytes("info"), Bytes.toBytes("a"))
        var proto = ProtobufUtil.toScan(scan)
        var scanToString = Base64.encodeBytes(proto.toByteArray());
        conf.set(TableInputFormat.SCAN, scanToString)
        conf.set(TableInputFormat.SCAN_ROW_START, "1")
        conf.set(TableInputFormat.SCAN_ROW_STOP, "3")
        val hbaseRdd = sc.newAPIHadoopRDD(conf, classOf[TableInputFormat], classOf[org.apache.hadoop.hbase.io.ImmutableBytesWritable], classOf[org.apache.hadoop.hbase.client.Result])
        println(hbaseRdd.count())
        hbaseRdd.foreach({ case (_, result) =>
            val cells = result.listCells()
            for (cell <- cells) {
                println(Bytes.toString(CellUtil.cloneRow(cell)), Bytes.toString(CellUtil.cloneQualifier(cell)), Bytes.toString(CellUtil.cloneValue(cell)))
            }
        })
        sc.stop()
    }

    /*
    * 使用 hBASE context 读取数据
    * */
    def read3(): Unit = {
        /* val hbasecontext=new HBaseContext(sc,conf)
         var scan = new Scan();
           // 这个函数有一个自定义重载的形式  支持你想要返回RDD 的格式  这就很好用了  一般组装成 json
         val hbaserdd=hbasecontext.hbaseRDD(TableName.valueOf("spark"),scan)
         hbaserdd.foreach({case (_,result)=>
           val cells=result.listCells()
           for (cell<-cells){
             println(Bytes.toString(CellUtil.cloneRow(cell)),Bytes.toString(CellUtil.cloneQualifier(cell)),Bytes.toString(CellUtil.cloneValue(cell)))
           }
         })
         sc.stop()*/
    }
}

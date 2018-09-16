package sparkdemo.firstwork.hbasepack

import org.apache.hadoop.conf.Configuration
import org.apache.hadoop.hbase.{HBaseConfiguration, TableName}
import org.apache.hadoop.hbase.client.{ConnectionFactory, Put, Table}
import org.apache.hadoop.hbase.mapred.TableOutputFormat
import org.apache.hadoop.hbase.mapreduce.TableInputFormat
import org.apache.hadoop.hbase.util.Bytes
import org.apache.hadoop.mapred
import org.apache.hadoop.mapred.JobConf
import org.apache.spark.sql.SparkSession

/**
  * 写入 Hbase
  */
object WriteHbase {
    val conf: Configuration = HBaseConfiguration.create()
    /**
      * 在这里完全可以看出访问 hbase 只需要zookeeper
      * 而且这个应该属于最原始的方法了
      */
    conf.addResource("habse.xml")
    conf.set(TableInputFormat.INPUT_TABLE, "spark")
    val sparksession = SparkSession.builder().master("local[2]").getOrCreate()
    val sc = sparksession.sparkContext

    def main(args: Array[String]): Unit = {
        write1()
    }

    def write1(): Unit = {
        val rdd = sc.textFile("/data/1.txt", 1)
        val data = rdd.map(_.split(","))
        val result = data.foreachPartition {
            x => {
                val table: Table = ConnectionFactory.createConnection(conf).getTable(TableName.valueOf("spark"))
                x.foreach { y => {
                    print(y.mkString("\t"))
                    var put = new Put(Bytes.toBytes(y(0)));
                    put.addColumn(Bytes.toBytes("info"), Bytes.toBytes("a"), Bytes.toBytes(y(1)));
                    put.addColumn(Bytes.toBytes("info"), Bytes.toBytes("b"), Bytes.toBytes(y(2)));
                    put.addColumn(Bytes.toBytes("info"), Bytes.toBytes("c"), Bytes.toBytes(y(3)));
                    table.put(put)
                }
                }
                table.close()
            }
        }
    }

    /*
    * 通过hnase 与 spark 的 API 进行操作 发现有点问题
    * */
    def write3(): Unit = {
        /* val jobConf:JobConf=new mapred.JobConf(conf)
         val rdd = sc.textFile("/data/1.txt",1)
         val data = rdd.map(_.split(","))
         jobConf.setOutputFormat(Class[TableOutputFormat])
         jobConf.set(TableOutputFormat.OUTPUT_TABLE,"spark")*/

    }

}

package sparkdemo.firstwork.hbasepack

import org.apache.hadoop.hbase.mapreduce.TableInputFormat
import org.apache.spark.sql.SparkSession
import org.apache.hadoop.hbase.{HBaseConfiguration, KeyValue}
import org.apache.hadoop.hbase.client.HTable
import org.apache.hadoop.hbase.io.ImmutableBytesWritable
import org.apache.hadoop.hbase.mapred.TableOutputFormat
import org.apache.hadoop.hbase.mapreduce.HFileOutputFormat
import org.apache.hadoop.hbase.util.Bytes
import org.apache.hadoop.mapreduce.Job

object SparkBulkload {

    val sc = SparkSession.builder().getOrCreate().sparkContext
    val conf = HBaseConfiguration.create()
    val tableName = "hao"
    val table = new HTable(conf, tableName)

    conf.set(TableOutputFormat.OUTPUT_TABLE, tableName)
    val job = Job.getInstance(conf)
    job.setMapOutputKeyClass(classOf[ImmutableBytesWritable])
    job.setMapOutputValueClass(classOf[KeyValue])
    HFileOutputFormat.configureIncrementalLoad(job, table)

    // Generate 10 sample data:
    val num = sc.parallelize(1 to 10)
    val rdd = num.map(x => {
        val kv: KeyValue = new KeyValue(Bytes.toBytes(x), "cf".getBytes(), "c1".getBytes(), "value_xxx".getBytes())
        (new ImmutableBytesWritable(Bytes.toBytes(x)), kv)
    })

    // Directly bulk load to Hbase/MapRDB tables.
    rdd.saveAsNewAPIHadoopFile("/tmp/xxxx19", classOf[ImmutableBytesWritable], classOf[KeyValue], classOf[HFileOutputFormat], job.getConfiguration())

    val hbaseRdd = sc.newAPIHadoopRDD(conf, classOf[TableInputFormat], classOf[org.apache.hadoop.hbase.io.ImmutableBytesWritable], classOf[org.apache.hadoop.hbase.client.Result])

}

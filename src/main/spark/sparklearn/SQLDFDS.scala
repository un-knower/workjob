package sparkdemo.sparklearn

import org.apache.spark.sql.SparkSession


object SQLDFDS {
    val sparkSession: SparkSession = SparkSession.builder().appName("BaseDemo").master("local[2]").getOrCreate()

    import sparkSession.implicits._

    def main(args: Array[String]): Unit = {
        mergeSchema()
    }

    /*分区发现和schema合并      相当于full join*/
    def mergeSchema(): Unit = {
        val squaresDF = sparkSession.sparkContext.makeRDD(1 to 5).map(i => (i, i * i)).toDF("value", "square")
        squaresDF.write.parquet("data/test_table/key=1")
        val cubesDF = sparkSession.sparkContext.makeRDD(6 to 10).map(i => (i, i * i * i)).toDF("value", "cube")
        cubesDF.write.parquet("data/test_table/key=2")
        val mergedDF = sparkSession.read.option("mergeSchema", "true").parquet("data/test_table")
        mergedDF.printSchema()
        mergedDF.show()
        /*发现写入的时候就会写入数据库——hive 但是在元数据库中没有*/
        mergedDF.write.partitionBy("key").saveAsTable("test")
        sparkSession.table("test").show()
    }


}

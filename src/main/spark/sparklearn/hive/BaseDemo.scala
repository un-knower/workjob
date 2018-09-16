package sparkdemo.sparklearn.hive

import org.apache.spark.sql.hive.HiveContext
import org.apache.spark.SparkConf
import org.apache.spark.SparkContext
import org.apache.spark.sql.SparkSession

object BaseDemo {

    val conf = new SparkConf().setAppName("Spark-Hive").setMaster("local")
    val spark = SparkSession
            .builder()
            .appName("Spark Hive Example").master("local[2]")
            /*.config("spark.sql.warehouse.dir", warehouseLocation)*/
            .enableHiveSupport()
            .getOrCreate()

    import spark.implicits._
    import spark.sql

    def main(args: Array[String]): Unit = {
        val DF = sql("select * from test")
        DF.show()
    }

    def test2(): Unit = {
        sql("CREATE TABLE IF NOT EXISTS src (key INT, value STRING) ")
    }

    def test1(): Unit = {
        val sc = new SparkContext(conf)
        val sqlContext = new HiveContext(sc)
        sqlContext.sql("CREATE TABLE IF NOT EXISTS src (key INT, value STRING) ROW FORMAT DELIMITED FIELDS TERMINATED BY '\t' ") //这里需要注意数据的间隔符
        sqlContext.sql("LOAD DATA INPATH '/user/liujiyu/spark/kv1.txt' INTO TABLE src  ")
        sqlContext.sql(" SELECT * FROM jn1").collect.foreach(println)
    }
}

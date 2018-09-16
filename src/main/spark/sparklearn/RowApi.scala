package sparkdemo.sparklearn

import org.apache.spark.sql.SparkSession

object RowApi {
    val ss = SparkSession.builder().master("local[2]").appName("row").getOrCreate()

    def main(args: Array[String]): Unit = {
        val data = Seq(("a", 1), ("b", 2))
        import ss.implicits._
        val tmp = ss.sparkContext.parallelize(data).toDF()
        tmp.show()
        tmp.foreach(row => {
            println(row(0))
            println(row.getInt(1))
        })
    }

}

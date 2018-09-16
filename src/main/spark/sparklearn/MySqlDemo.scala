package sparkdemo.sparklearn

import java.util.Properties

import org.apache.spark.sql.{DataFrame, Dataset, SaveMode, SparkSession}


object MySqlDemo {
    private var mysqlConf = new Properties()
    mysqlConf.setProperty("driver", "com.mysql.jdbc.Driver")
    mysqlConf.setProperty("user", "root")
    mysqlConf.setProperty("password", "www1234")
    val sparkSession: SparkSession = SparkSession.builder().appName("Base Demo").master("local[2]").getOrCreate()
    val url = "jdbc:mysql://ts1:3306/kingcall?&serverTimezone=UTC&characterEncoding=utf8"


    def main(args: Array[String]): Unit = {
        val df = readFromMysql(sparkSession, url, "danmaku_lag", mysqlConf)

    }

    def readFromMysql(sparkSession: SparkSession, url: String, tablename: String, mysqlConf: Properties): DataFrame = {
        val df = sparkSession.read.jdbc(url, tablename, mysqlConf)
        df.show()
        df
    }

    def writeToMysql(resultdataFrame: DataFrame, tablename: String, sparkSession: SparkSession = sparkSession, url: String = url, mysqlConf: Properties = mysqlConf): Unit = {
        val p = resultdataFrame.write.mode(SaveMode.Append).jdbc(url, tablename, mysqlConf)
        println(p)

    }

    def executeQuery(queryDataFrame: DataFrame, sparkSession: SparkSession, sql: String, viewName: String): DataFrame = {
        if (!sql.contains(viewName))
            throw new Throwable("the tablename is not in your query sql,you should be careful")
        queryDataFrame.createTempView(viewName)
        sparkSession.sql(sql)
    }

}

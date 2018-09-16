package sparkdemo.sparklearn

import java.util.Properties

import org.apache.spark.sql.{DataFrame, SaveMode, SparkSession}

object MysqlUtil {
    var mysqlConf = new Properties()
    mysqlConf.setProperty("driver", "com.mysql.jdbc.Driver")
    mysqlConf.setProperty("user", "root")
    mysqlConf.setProperty("password", "www1234")


    def saveToMysql(DF: DataFrame, tablename: String): Unit = {
        DF.write.mode(SaveMode.Append).jdbc("jdbc:mysql://localhost:3306/test?useUnicode=true&amp;characterEncoding=UTF8&serverTimezone=UTC&useSSL=true", tablename, mysqlConf)
    }


}

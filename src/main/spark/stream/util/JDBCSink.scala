package stream.util

import java.sql.{Connection, DriverManager, Statement}

import org.apache.spark.sql.ForeachWriter

/**
  * 将结构化流输出到mysql中去
  */
class JDBCSink(url:String, user:String, pwd:String) extends ForeachWriter[Row]{
    val driver = "com.mysql.jdbc.Driver"
    var connection:Connection = _
    var statement:Statement = _
    override def open(partitionId: Long, version: Long): Boolean =  {
        Class.forName(driver)
        connection = DriverManager.getConnection(url, user, pwd)
        statement = connection.createStatement
        true
    }
    override def close(errorOrNull: Throwable): Unit = connection.close()

    override def process(value: Row): Unit = {
        statement.executeUpdate(s"delete from test where room_id=${value.getString(0)}")
        statement.executeUpdate(s"insert into test values (${value.getString(0)},${value.getLong(1)})")
        println(value+"\t 数据插入完毕")
    }
}

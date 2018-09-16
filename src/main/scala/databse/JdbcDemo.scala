package databse

import java.sql.{Connection, DriverManager}

object JdbcDemo {
    val url = "jdbc:mysql://localhost:3306/kingcall?useUnicode=true&characterEncoding=utf-8&useSSL=false"
    val driver = "com.mysql.jdbc.Driver"
    val username = "root"
    val password = "www1234"
    var connection: Connection = null
    def main(args: Array[String]): Unit = {
        prepareStatmentQuery()
    }
    def prepareStatmentQuery(): Unit ={
        try {
            Class.forName(driver)
            connection = DriverManager.getConnection(url, username, password)
            val sql="select * from student where id=? "
            val statement = connection.prepareStatement(sql)
            statement.setInt(1,100)
            val rs = statement.executeQuery()
            //打印返回结果
            while (rs.next) {
                val id = rs.getInt("id")
                val `class` = rs.getString("banji")
                val name = rs.getString("name")
                val sex = rs.getString("sex")
                println("id=%d,name = %s, class = %s,sex=%s".format(id,name, `class`,name,sex))
            }
        } catch {
            case e: Exception => e.printStackTrace
        }
        //关闭连接，释放资源
        connection.close

    }

    def test1(): Unit ={
        try {
            //注册Driver
            Class.forName(driver)
            //得到连接
            connection = DriverManager.getConnection(url, username, password)
            val statement = connection.createStatement
            //执行查询语句，并返回结果
            val rs = statement.executeQuery("SELECT * FROM student")
            //打印返回结果
            while (rs.next) {
                val id = rs.getInt("id")
                val `class` = rs.getString("banji")
                val name = rs.getString("name")
                val sex = rs.getString("sex")
                println("id=%d,name = %s, class = %s,sex=%s".format(id,name, `class`,name,sex))
            }
            println("查询数据完成！")
            //    执行插入操作

            val rs2 = statement.executeUpdate("INSERT INTO student(id,banji,name,sex) values(100,'三班','king','男')")
            println("插入数据完成")


        } catch {
            case e: Exception => e.printStackTrace
        }
        //关闭连接，释放资源
        connection.close
    }

}

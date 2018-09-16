package databse

/**
  * Slick是集成jdbc的更高层的Query编程语言，可以通过jdbc的url、DataSource等来指定目标数据库类型及相关的参数。
  * FRM相比ORM最明显的优势就是FRM基于多线程的Future的数据查询，而ORM是单线程的线性执行
  */
import slick.driver.MySQLDriver.api._
import scala.concurrent.ExecutionContext.Implicits.global
object SlickDemo {
    // 表字段对应的模板
    case class CampaignModel(id :Int,banji:String,name:String,sex:String)
    // config database
    val db = Database.forURL(
        url = "jdbc:mysql://localhost:3306/kingcall?useUnicode=true&characterEncoding=UTF-8&useSSL=false",
        driver = "com.mysql.jdbc.Driver",
        user = "springuser",
        password = "ThePassword")
    //表结构: 定义字段类型, * 代表结果集字段
    class table(tag: Tag) extends Table[CampaignModel](tag,"sad"){
        def id=column[Int]("id")
        def banji=column[String]("banji")
        def name=column[String]("name")
        def sex=column[String]("sex")
        override def * = (id,banji,name,sex)<>(CampaignModel.tupled, CampaignModel.unapply)
    }

    def main(args: Array[String]): Unit = {
        val coffees = TableQuery[table]
       coffees.result
    }




}

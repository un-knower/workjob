package scala.other.jsondm

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.scala.DefaultScalaModule

object JsonTest {
    def main(args: Array[String]): Unit = {
        val mapper = new ObjectMapper()
        mapper.registerModule(DefaultScalaModule)
        val log = "{\"status\":200,\"http_user_agent\":\"Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/50.0.2661.102 Safari/537.36\",\"request_method\":\"get\",\"timestamp\":\"2016-11-13T06:59:26.315Z\",\"http_url\":\"http://www.donews.com/it/201611/2942496.shtm\",\"http_x_forwarded_for\":\"221.0.90.218\",\"event\":\"load\",\"is_new\":1,\"http_referer\":\"https://www.baidu.com/link?url=btPCeZwsYyyONB-oYL7szsox4qjvynFPzoeZlocefakSUxZxj0gEOXoG9dIivWg7BqsooA1tMfZpS-FYdl5Gva&wd=&eqid=c3101a590000f2980000000258280f0d\",\"cookie\":\"441de3213207ac61f58be158e80cc194\",\"page_id\":\"23788cd5e93c8c870472bd1bd40f6c40\",\"short_cookie\":\"2cb494930448e4abf37577dd28c5947f\",\"appkey\":\"app_website\"}"
        val obj = mapper.readValue(log, classOf[SdkBean])
        println(obj.toString)

    }
}

package sparkdemo.firstwork.hbasepack

import java.io.{BufferedReader, File, FileReader}

import com.alibaba.fastjson.{JSON, JSONObject}
import sun.misc.{BASE64Decoder, BASE64Encoder}


object PrePareData {
    val encoder = new BASE64Encoder
    val decoder = new BASE64Decoder
    val file = new File("D:\\workingspace\\Code\\workjob\\src\\main\\resources\\mbchat.txt")
    val br: BufferedReader = new BufferedReader(new FileReader(file))

    def main(args: Array[String]): Unit = {
        var s = br.readLine()
        while (s != null) {
            val firstobj = JSON.parseObject(s)
            chat(firstobj)
            s = br.readLine()
        }
    }

    /**
      * 解析chat 数据
      *
      * @param fisrtObject
      * 发现 即使是聊天的 消息对象那个的key,roomid 存在不确定 但是可以看到文档上是对 room id 是没有要求的
      */
    def chat(fisrtObject: JSONObject): Unit = {
        val tmpobj = JSON.parseObject(new String(decoder.decodeBuffer(fisrtObject.getString("msg"))))
        if (tmpobj.get("type").equals("chat")) {
            println("==" + tmpobj.keySet())
            // 这个字段
            val messageid = tmpobj.getString("id")
            val msgobj = JSON.parseObject(tmpobj.getString("msg"))
            val user = msgobj.getJSONObject("user")
            // 发现这个对象可能为空
            val medal = msgobj.getJSONObject("medal")
            val via = msgobj.getString("via")
            val color = msgobj.getString("color")
            val style = msgobj.getString("style")
            val sportRoomId = msgobj.getString("sportRoomId")
            val content = msgobj.getString("content")
            try {
                println(messageid, user.getString("uid"), user.getString("username"), user.getString("grade"), via, content, color, style, sportRoomId, medal == null)
            } catch {
                case ex: NullPointerException => println(user.toJSONString, medal.toJSONString)
            }
        }
    }

    def gift(fisrtObject: JSONObject): Unit = {
        val tmpobj = JSON.parseObject(new String(decoder.decodeBuffer(fisrtObject.getString("msg"))))
        if (tmpobj.get("type").equals("broadcastEnd")) {
            println(fisrtObject.keySet())
            println("==" + tmpobj.keySet())
        }
    }
}

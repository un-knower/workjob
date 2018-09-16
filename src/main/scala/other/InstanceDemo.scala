package scala.other

/**
  * classOf、isInstanceOf、asInstanceOf三个预定义方法分析
  */
object InstanceDemo {
    def main(args: Array[String]): Unit = {
        println("hello".isInstanceOf[String])
        val c: Char = 97.asInstanceOf[Char]
        println(c)
        // ClassOf 获取类型T的Class对象
        println(classOf[String])
        println(c.getType)
    }

}

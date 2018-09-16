package scala.Book.mydone.scala2.ch01


/**
  * @Authork:kingcall
  * @Description:
  * @Date:$date$ $time$
  */
object test {
    var str = "sdfsd"
    var ptr = "srfrstudsv"

    val x, y, z = 1000
    val p = Array(1, 2, 3, 4)

    def main(args: Array[String]): Unit = {
        println(str)
        println(x + "\t" + y + "\t" + z)
        // 有一定的统一的效果在里面，字符串数组都是用圆括号，而python中都是用方括号
        println(str(2))
        println(p(1))

        println(str.contains("fsd"))

        println(ptr.containsSlice('r'.to('v')))

        println(10.max(2))
    }

}

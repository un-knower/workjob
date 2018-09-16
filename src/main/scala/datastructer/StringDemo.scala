package datastructer

/**
  * 字符串的几种输出类型
  */
object StringDemo {
    def main(args: Array[String]): Unit = {
        test1()
        test2()
    }

    /**
      * 针对原生字符串 idea 自动帮你调用了 stripMargin 你可能失去了你想要的哪些空格
      */
    def test1(): Unit ={
        println(
            """
              |多福多寿
              |水电费水电费
            """.stripMargin)
    }
    def test2(): Unit ={
        val s=
            """
              12345
                234567
            """
        println(s)
    }
}

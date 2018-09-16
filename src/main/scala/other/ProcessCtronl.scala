package scala.other


object ProcessCtronl {
    /*
    * 学习一下这种循环的特点
    * */
    def main(args: Array[String]): Unit = {
        val t = Array("spark", "python", "java")
        val s = Set("spark", "scala", "java", "hadoop")
        val result = for {
            i <- s
            if t.contains(i)
        } yield {
            i
        }

        println(
            """
              |yield的用法总结
              |针对每一次 for 循环的迭代, yield 会产生一个值，被循环记录下来 (内部实现上，像是一个缓冲区).
              |当循环结束后, 会返回所有 yield 的值组成的集合.
              |返回集合的类型与被遍历的集合类型是一致的.
            """.stripMargin)
        println(result)
        println("遍历结果")
        result.foreach(println(_))
    }

}

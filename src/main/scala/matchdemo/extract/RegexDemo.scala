package matchdemo.extract

import scala.util.matching.Regex

/**
  * 论正则表达式的修养
  */
object RegexDemo {

    def test1(): Unit ={

    }

    /**
      * 构建正则对象的几种方式
      */
    def getRegex(): Unit ={
        val d1=new Regex("\\d\\d\\d")
        val d2=new Regex("""\d\d\d""")
        val d3="""\d\d\d""".r
    }
}

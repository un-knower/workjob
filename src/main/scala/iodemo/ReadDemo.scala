package scala.iodemo

import scala.collection.mutable
import scala.io.Source

object ReadDemo extends App {


    def reads(): Unit = {
        /* 把文件读成了一个可迭代对象*/
        val itor = Source.fromFile("C:\\Users\\PLUSH80702\\Desktop\\10.10.10.110.txt")
        println(itor.getLines().size)

        while (itor.hasNext) {
            println(itor.next())
        }
    }

    def readProp(fileName: String): mutable.Map[String, String] = {
        val prop = mutable.Map[String, String]()
        val kv = (s: String) => {
            val idx = s.indexOf("=")
            prop.+=(s.substring(0, idx) -> s.substring(idx + 1, s.length))
        }
        Source.fromInputStream(
            this.getClass.getClassLoader.getResource(fileName).openStream()
        ).getLines().filterNot(_.startsWith("#")).foreach(kv)
        prop

    }

}

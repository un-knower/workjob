package scala.collection

import scala.collection.immutable.HashMap

object Mapdm {
    def main(args: Array[String]): Unit = {
        test2()
    }

    def addothers(v1: mutable.HashMap[String, Int], v2: mutable.HashMap[String, Int]): Unit = {

    }

    def test2(): Unit = {
        val p = HashMap("a" -> 1, "b" -> 2, "c" -> 3, "d" -> 4, "e" -> 5, "f" -> 6, "g" -> 7)
        p.map(print(_))
    }

}

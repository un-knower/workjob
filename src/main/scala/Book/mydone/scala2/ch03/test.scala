package scala.Book.mydone.scala2.ch03

import scala.collection.mutable.ArrayBuffer

/**
  * for 循环来遍历数组可能是出于其他目的 例如 每隔一个元素输出一次
  */
object test {
    def main(args: Array[String]): Unit = {
        other_method
    }

    /**
      * 数组遍历的三种方式
      */
    def base2(): Unit = {
        val x = Array("a", "b", "c", "d")
        for (i <- x) {
            println(i)
        }
        for (i <- 0 to x.length - 1) {
            println(x(i))
        }
        // 这才是遍历数组的真爱
        for (i <- 0 until x.length) {
            println(x(i))
        }

        val y = 10 to 0 by -1
        println(y)

    }

    /**
      * 数组的扩展
      */
    def base1(): Unit = {
        val x1 = Array(1, 2, 3, 4)
        val x2 = new Array[Int](10) //必须指定长度，其实很容易理解
        import scala.collection.mutable.ArrayBuffer
        val b = ArrayBuffer[Int]()
        b += 1
        println(b)
        b += (2, 3, 4, 5)
        println(b)
        b ++= Array(6, 7, 8)
        println(b)
    }

    /**
      * for 推导式 以及该推导式的filter-map实现
      */
    def ford(): Unit = {
        val x = Array(1, 2, 3, 4, 5, 6, 7, 8, 9)
        val z = for (i <- x if i % 2 == 0) yield i
        for (i <- z) println(i)

        val z2 = x.filter(_ % 2 == 0)
        for (i <- z2) println(i)
    }

    def other_method(): Unit = {
        val a = ArrayBuffer(1, 2, 3, 4, 5, 6, 7, 8, 9)
        println(a.count(_ % 2 == 0))
        a.append(10)
        println(a.mkString("<<", "\t", ">>"))
        // 后面被包含的内容是指连续的情况
        println(a.containsSlice(Array(4, 5, 6, 8)))
    }

    def testdifferencejava(): Unit = {
        val x = Array("a", "b", "c", "d", "e", "f", "g")
        // print(java.util.Arrays.binarySearch(x,"d"))

    }


}

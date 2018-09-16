package scala.Book.mydone.scala2.ch03

import java.util.Random

object after {
    def main(args: Array[String]): Unit = {
        two
    }

    def one(n: Int): Unit = {
        val a = new Array[Int](n)
        for (i <- 0 until a.length) {
            a(i) = new Random().nextInt(n)
        }
        println(a.mkString("\t"))
    }

    def two(): Unit = {
        val arr = Array(1, 2, 3, 4, 5, 6, 7, 8, 9)
        println(arr.mkString("\t"))
        if (arr.length % 2 == 0) {
            for (i <- 0 to arr.length - 2 by 2) {
                val tmp1 = arr(i)
                arr(i) = arr(i + 1)
                arr(i + 1) = tmp1
            }
        } else {
            for (i <- 0 to arr.length - 3 by 2) {
                val tmp1 = arr(i)
                arr(i) = arr(i + 1)
                arr(i + 1) = tmp1
            }

        }
        println(arr.mkString("\t"))
    }

}

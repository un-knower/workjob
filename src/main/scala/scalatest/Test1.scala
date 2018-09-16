package scala.scalatest

import org.scalatest.{BeforeAndAfter, FlatSpec, Matchers}


object Test1 extends FlatSpec with Matchers with BeforeAndAfter {
    var wasStopped = false
    before {
        wasStopped = false
    }
    after {
        if (!wasStopped) {
            println("hello")
        }
    }

    def main(args: Array[String]): Unit = {
        println("hello world")
        wasStopped = true
    }

}

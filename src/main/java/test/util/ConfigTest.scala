package test.util

import constant.JobConfigDef._

object ConfigTest {

    def main(args: Array[String]): Unit = {
    }

    def test(): Unit = {
        val map = Map("a" -> 1, "b" -> 2, "c" -> 3)
        println(map(JOB_NAME))
    }

}

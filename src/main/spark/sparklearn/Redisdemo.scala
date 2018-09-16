package sparkdemo.sparklearn

import com.redislabs.provider.redis._
import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}

/*
* 这个中没有什么难度，只是要熟悉几个调用的方法即可
* */
object Redisdemo {
    val conf = new SparkConf().setMaster("local[2]").setAppName("sparkRedisTest")
    conf.set("redis.host", "localhost")
    conf.set("redis.port", "6379")
    val sc = new SparkContext(conf)

    def main(args: Array[String]): Unit = {
        hash_op()
    }

    def wcread(): Unit = {
        val tmp = sc.fromRedisZSetWithScore("wc")
        tmp.foreach(println(_))
    }

    /*充分利用zset的特性  k  score（单词个数）*/
    def wcwrite(): Unit = {
        val tmp: Array[(String, String)] = Array(("a", "1"), ("b", "2"), ("c", "3"))
        println(tmp.mkString("="))
        val wc = sc.parallelize(tmp)
        sc.toRedisZSET(wc, "wc")
    }

    /* 演示hash 的相关特性 */
    def hash_op(): Unit = {
        val s = sc.fromRedisHash("king").map(x => {
            (x._1, x._2.toInt)
        })
        val s1: RDD[(String, Int)] = sc.parallelize(Seq(("a", 1), ("c", 1)))
        val result = s.fullOuterJoin(s1).map(x => {
            val key = x._1
            val value = x._2._1.getOrElse(0) + x._2._2.getOrElse(0)
            (key, value.toString)
        })
        result.foreach(println(_))
        sc.toRedisHASH(result, "king")
    }
}

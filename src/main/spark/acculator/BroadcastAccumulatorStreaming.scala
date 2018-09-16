package acculator

import org.apache.spark.broadcast.Broadcast
import org.apache.spark.streaming.{Duration, StreamingContext}
import org.apache.spark.util.LongAccumulator
import org.apache.spark.{SparkConf, SparkContext}

/**
  * 广播一个比较大的变量
  *     优化点：对象的序列化
  *     使用广播变量代替耗时的join操作
  *     一个Executor只需要在第一个Task启动时，获得一份Broadcast数据，之后的Task都从本地的BlockManager中获取相关数据
  */
object BroadcastAccumulatorStreaming {
    /**
      * 声明一个广播和累加器！
      */
    private var broadcastList: Broadcast[List[String]] = _
    private var accumulator: LongAccumulator = new LongAccumulator
    val sparkConf = new SparkConf().setMaster("local[4]").setAppName("broadcasttest")
    def main(args: Array[String]) {
      test1()
    }

    /**
      * 根据广播变量对数据过滤，将过滤后的结果统计到累加器
      */
    def test1(): Unit ={

        val sc = new SparkContext(sparkConf)
        broadcastList = sc.broadcast(List("a", "b"))
        sc.register(accumulator, "broadcasttest")
        val lines = sc.parallelize(Array("a","a","a","b","b","c","c","d"),3)
        val words = lines.flatMap(line => line.split(" "))
        val wordpair = words.map(word => (word, 1))
        // 利用 broadcast 进行数据过滤
        val result=wordpair.filter(record => {
            broadcastList=sc.broadcast(List("a","b","d"))
            broadcastList.value.contains(record._1)
        })
        //对过滤后的值进行累加
        val pair = result.reduceByKey(_ + _)
        // 改变累加器的值
        pair.foreach(x=>accumulator.add(x._2))
        println("累加器的值" + accumulator.value)
    }

}

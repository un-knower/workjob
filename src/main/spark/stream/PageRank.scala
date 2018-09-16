package stream

import java.net.URL

import org.apache.spark.{HashPartitioner, Partitioner, SparkConf}
import org.apache.spark.streaming.{Seconds, StreamingContext}

/**
  * 从分区获益的复杂操作
  *     1. 网页排序
  *     2. 科技文章排序
  *     3. 社交网站上的重要用户排序
  * 理论基础
  *     如果一个网页被很多其他网页链接到的话说明这个网页比较重要，也就是PageRank值会相对较高(很多网页都会给它贡献)
  *     如果一个PageRank值很高的网页链接到一个其他的网页，那么被链接到的网页的PageRank值会相应地因此而提高（它每次贡献的值比较高）
  *     PageRank算法总的来说就是预先给每个网页一个PR值（下面用PR值指代PageRank值），由于PR值物理意义上为一个网页被访问概率，所以一般是1/N，其中N为网页总数,但是这里给了1
  */
object PageRank {
    val conf = new SparkConf().setAppName("stream learn").setMaster("local[2]")
    val ssc = new StreamingContext(conf, Seconds(2))
    val sc = ssc.sparkContext
    def main(args: Array[String]): Unit = {
        rank2
    }

    /**
      * 利用hash分区
      */
    def rank1(): Unit ={
        val links=sc.objectFile[(String,Seq[String])]("links").partitionBy(new HashPartitioner(100)).persist()
        // 下面这个步骤很关键 保持了分区一致
        var ranks=links.mapValues(v=>1.0)
        for(i<-0 until 10){
            // 所有的页面都有一个或多个贡献值
            val contributes=links.join(ranks).flatMap {
                case (pageid, (links, rank)) =>links.map(dest=>(dest,rank/links.size))
            }
            //根据页面id,将每个页面的贡献值求和，后面对每个求和之后的值进行处理（为什么要这么处理）
            ranks=contributes.reduceByKey((x,y)=>x+y).mapValues(v=>0.15+0.85*v)
        }
        ranks.saveAsTextFile("ranks")
    }

    /**
      * 自定分区实现
      *     由于上面对url 进行哈希的方式可能会把同一个域名（有更高的可能性会有链接关系）下的不同url 分在不同的分区中，所以自定义分区会更好
      */
    def rank2(): Unit = {
        class mypartitoner(numParts: Int) extends Partitioner {
            // 要创建的分区个数
            override def numPartitions: Int = numParts
            // 返回给定键的分区编号（也就是根据这个函数来确定元素的分区）
            override def getPartition(key: Any): Int ={
                val url=new URL(key.toString).getHost.split(".")(1)
                url.hashCode%numPartitions
            }
        }
        val links=sc.objectFile[(String,Seq[String])]("links").partitionBy(new mypartitoner(100)).persist()
        // 下面这个步骤很关键 保持了分区一致
        var ranks=links.mapValues(v=>1.0)
        for(i<-0 until 10){
            // 所有的页面都有一个或多个贡献值
            val contributes=links.join(ranks).flatMap {
                case (pageid, (links, rank)) =>links.map(dest=>(dest,rank/links.size))
            }
            //根据页面id,将每个页面的贡献值求和，后面对每个求和之后的值进行处理（为什么要这么处理）
            ranks=contributes.reduceByKey((x,y)=>x+y).mapValues(v=>0.15+0.85*v)
        }
        ranks.saveAsTextFile("ranks")

    }

}

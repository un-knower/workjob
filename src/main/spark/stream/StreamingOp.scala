package sparkdemo.streaming

import org.apache.spark.rdd.RDD
import org.apache.spark.sql.SparkSession
import org.apache.spark.storage.StorageLevel
import org.apache.spark.streaming.dstream.{DStream, ReceiverInputDStream}

import scala.collection.mutable.Queue
import scala.collection.mutable.Set

/* 官网学习 spark-stream
 * 同时验证spark的各种算子
 *
 * 在普通 rdd 的action 算子在pairRdd 中往往是转换算子
 * 实现WC 的操作
 *      1. reduceBuKey
 *      2.countByValue
 *      3.CountByKey
 * */
object StreamingOp {

    import org.apache.spark._
    import org.apache.spark.streaming._

    val conf = new SparkConf().setAppName("stream learn").setMaster("local[2]")
    val ssc = new StreamingContext(conf, Seconds(2))
    val sc = ssc.sparkContext
    val ss=SparkSession.builder().config(conf).getOrCreate()


    def main(args: Array[String]): Unit = {
        glom_op()
    }
    def monitor_dir(): Unit ={
        val lines = ssc.textFileStream("D:\\logs")
        lines.foreachRDD(rdd=>{
            rdd.foreach(println(_))
        })
        ssc.start()
        ssc.awaitTermination()
    }

    /*关于监控*/
    def monitor(): Unit = {
        ssc.addStreamingListener(new StreamListener())
    }

    /*验证各种算子*/
    def base_wc_socket(): Unit = {
        //monitor()
        ssc.checkpoint("D:/checkpoint") // 这个有时候需要，有时候可以没有
        val lines = ssc.socketTextStream("localhost", 9999, StorageLevel.MEMORY_AND_DISK_SER)
        join_op_stream(lines)
        ssc.start();
        ssc.awaitTermination()
    }

    /*为什么可以不断输出呢*/
    def rdds_to_stream(): Unit = {
        val queue = Queue[RDD[String]]()
        val rdd1 = ssc.sparkContext.textFile("src/main/resources/static.json")
        val rdd2 = ssc.sparkContext.textFile("src/main/resources/static.json")
        val rdd3 = ssc.sparkContext.textFile("src/main/resources/static.json")
        queue += rdd1;
        queue += rdd2;
        queue += rdd3;
        val lines = ssc.queueStream(queue)
        lines.foreachRDD(rdd => {
            rdd.foreach(println(_))
        })
        ssc.start()
        ssc.awaitTermination()
    }

    /*
    * 也有一个参数就是task 的个数其实就是分区的个数-------WC
    * reduceByKey 就是针对pairRdd 的Reduce 操作,但是reduceByKey 是个 transformation
    * */
    def reduce_by_key(lines: ReceiverInputDStream[String]): Unit = {
        lines.flatMap(_.split(" ")).map(word => (word, 1)).reduceByKey(_ + _).print()
    }

    /**
      * reduce 是个action操作，因为它的返回不是rdd 并且reduce 的返回值要和rdd 一致
      * reduce 不止可以用来求和求极，也可以用来求最大最小值
      */
    def reduce_op(): Unit ={
        val s=Array(1,2,3,4,5,6)
        val rdd=sc.parallelize(s)
        val sum=rdd.reduce(_+_)
        println(sum)
        val s2=Array(("a",1),("b",2),("C",3),("a",2),("b",1))
        val rdd2=sc.parallelize(s2)
        val sum2=rdd2.reduceByKey(_+_)
        show_parttion_pair(sum2)
    }

    /**
      * reduce 不止可以用来求和求极，也可以用来求最大最小值  所以reduceByKey 也可以有同样的操作
      */
    def reduce_max_min(): Unit ={
        val s=Array(1,2,3,4,5,6)
        val rdd=sc.parallelize(s)
        val max=rdd.reduce((x,y)=>if(x>y)x  else y)
        val min=rdd.reduce((x,y)=>if(x<y)x  else y)
        println((max,min))

    }
    /**
      * reduce 用来求平均值
      */
    def reduce_mean(): Unit ={
        val s=Array(1,2,3,4,5,6,7,8,9,10)
        val rdd=sc.parallelize(s)
        val keyvalue=rdd.map(x=>(1,x))
        val (count,sum)=keyvalue.reduce((x,y)=>(x._1+y._1,x._2+y._2))
        println((count,sum))

    }

    /*
    * RDD[String] --> RDD[String long]   long 是value出现的次数  参数是处理后的分区个数------WC
    * */
    def count_by_value(lines: ReceiverInputDStream[String]): Unit = {
        lines.flatMap(_.split(" ")).countByValue(1).print()
    }

    /**
      * stream 没有这个算子
      */
    def count_by_key(): Unit ={
        val rdd1 = sc.parallelize(List(("a", 1), ("b", 2), ("b", 2), ("c", 2), ("c", 1)))
        val result=rdd1.countByValue()
        val result2=rdd1.countByKey()
    }

    /**
      * 这个才是正确使用 wc 的节奏
      */
    def count_by_value(): Unit ={
        val rdd1 = sc.parallelize(List("a","b","c","d"))
        val result=rdd1.countByValue()
        result.foreach(println(_))
    }

    /**
      * 如果对某个键返回 None 则对该键不在维护状态
      * @param lines
      */
    def upstate_by_key(lines: ReceiverInputDStream[String]): Unit = {
        val addFunc = (values: Seq[Int], state: Option[Int]) => {
            Some(values.sum + state.getOrElse(0))
        }
        val pair = lines.flatMap(_.split(" ")).map(x => (x, 1))
        val totalWordCounts = pair.updateStateByKey[Int](addFunc)
        totalWordCounts.print()
        totalWordCounts.checkpoint(Seconds(8))
    }

    /*本质上是将 key 相等作为条件的，但是和日常的不同之处就是在SQL中做了聚合,然后再做join ,但是这里并没有，所以结果看起来有点意外，其实并没有， 有了外联之后是option组合成的值 */
    def join_op_stream(lines: ReceiverInputDStream[String]): Unit = {
        val x1 = lines.flatMap(_.split(" ")).map(x => (x, 1))
        val x2 = lines.flatMap(_.split(" ")).map(x => (x, 1)).union(ssc.queueStream(Queue.apply[RDD[(String, Int)]](ssc.sparkContext.parallelize[(String, Int)](Array.apply(("kingcall", 200))))))
        val x = x2.fullOuterJoin(x1)
        x.print()
    }

    /**
    * join 的升级版本  将key相同的先聚合(形成了值的序列) 然后再join  (b,(CompactBuffer(1, 1, 1, 1),CompactBuffer(1, 1, 1, 1)))
      * 其实就是group 的作用
    *
    */
    def cogroup_op(lines: ReceiverInputDStream[String]): Unit = {
        val x1 = lines.flatMap(_.split(" ")).map(x => (x, 1))
        val x2 = lines.flatMap(_.split(" ")).map(x => (x, 1))
        val x = x1.cogroup(x2)
        x.print()
    }

    /*纯粹的窗口等价于 batch 的延长，就是将多个RDD整合成一个*/
    def join_window(lines: ReceiverInputDStream[String]): Unit = {
        val x1 = lines.flatMap(_.split(" ")).map(x => (x, 1)).window(Seconds(10))
        val x2 = lines.flatMap(_.split(" ")).map(x => (x, 1)).window(Seconds(10))
        val x = x1.cogroup(x2)
        x.print()
    }

    /*window  + reduceByKey 都可以指定分区也就是任务的并行度*/
    def reduce_ByKey_AndWindow(lines: ReceiverInputDStream[String]): Unit = {
        val pair = lines.flatMap(_.split(" ")).map(x => (x, 1))
        val wc = pair.reduceByKeyAndWindow((a: Int, b: Int) => (a + b), Seconds(10), Seconds(2), 1)
        wc.print()
    }

    def count_ByValue_AndWindow(lines: ReceiverInputDStream[String]): Unit = {
        val pair = lines.flatMap(_.split(" "))
        val wc = pair.countByValueAndWindow(Seconds(10), Seconds(5))
        wc.print()
    }

    /*就是计数，但是没有区分到底是哪个的数量     直观上没用*/
    def count_ByWindow(lines: ReceiverInputDStream[String]): Unit = {
        val pair = lines.flatMap(_.split(" "))
        val wc = pair.countByWindow(Seconds(10), Seconds(5))
        wc.print()
    }

    /* 下面的计算结果不准确  不能分别计算对应的key  会把所有的key 计算到一起*/
    def reduce_by_window(lines: ReceiverInputDStream[String]): Unit = {
        val pair = lines.flatMap(_.split(" ")).map(x => (x, 1))
        val kingcall = (a: (String, Int), b: (String, Int)) => (a._1, a._2 + b._2)
        val wc = pair.reduceByWindow(kingcall, Seconds(4), Seconds(2))
        wc.print()
    }

    /*  transform  的强大之处在于流的转换——目前唯一您能拿到RDD  操作且能拿到最后留对象的方法*/
    def transform_op(lines: ReceiverInputDStream[String]): Unit = {
        val pairs: DStream[(String, Int)] = lines.flatMap(_.split(" ")).transform(rdd => {
            rdd.map(x => (x, 1))
        })
        pairs.print()
    }

    /* 每一个key 对应着一串值（key,(1,1,1,2,1,)）*/
    def groupByKey_op(lines: ReceiverInputDStream[String]): Unit = {
        val pairs: DStream[(String, Iterable[Int])] = lines.flatMap(_.split(" ")).map(x => (x, 1)).groupByKey()
        pairs.print()
        val wc = pairs.map(x => (x._1, x._2.sum))
        wc.print()
    }

    /**
      * combineByKey是Spark中一个比较核心的高级函数，其他一些高阶键值对函数底层都是用它实现的。诸如 groupByKey,reduceByKey
      * 按key的功能由spark提供，每当一条数据进来之，如果不存在Combiner 则创建 Combiner，再进来的数据根对应key的合并规则和Combiner合并，完成之后各个分区的Combiner再合并
      * Combine values with the same key using a different result type.
      *
      */
    def combineByKey_op(): Unit = {
        val initialScores = Array(("Fred", 88.0), ("Fred", 95.0), ("Fred", 91.0), ("Wilma", 93.0), ("Wilma", 95.0), ("Wilma", 98.0))
        val d1 = sc.parallelize(initialScores)
        type MVType = (Int, Double) //定义一个元组类型(科目计数器,分数)
        d1.combineByKey(
            score => (1, score),
            (c1: MVType, newScore) => (c1._1 + 1, c1._2 + newScore),
            (c1: MVType, c2: MVType) => (c1._1 + c2._1, c1._2 + c2._2)
        ).collect.foreach(println(_))
    }

    /**
      * combineByKey 达到 reduceByKey的效果，完成wc
      * 记住一点：按key的功能不用管
      */
    def combineByKey_WC(): Unit = {
        val initialScores = Array(("Fred", 2), ("Fred", 1), ("Fred", 3), ("Wilma", 2), ("Wilma", 5), ("Wilma", 3))
        val d1 = sc.parallelize(initialScores, 2)
        val result = d1.combineByKey(
            cnt => (cnt),
            (wc1: Int, cnt) => wc1 + cnt,
            (wc1: Int, wc2: Int) => wc1 + wc2
        )
        result.foreach(println(_))
    }

    /**
      * 求最大值（也可以求分区最大值的和）
      *     1. 聚合操作
      *     2.分区内 reduce 操作
      *     3.合并分区结果
      */
    def aggregate_op(): Unit = {
        val rdd = sc.parallelize(List(1, 2, 3, 4, 5, 6, 7, 8, 9), 2)
        val result = rdd.aggregate(0)(
            math.max(_, _),
            math.max(_, _)
        )
        println(result)
    }

    /**
      * 和 reduce 相比就是返回值的类型可以和rdd 的类型不一致
      */
    def aggregate_op2(): Unit = {
        val rdd = sc.parallelize(List(1, 2, 3, 4, 5, 6, 7, 8, 9), 2)
        val result = rdd.aggregate(0)(
            (_+_),
            (_+_)
        )
        println(result)
    }

    /**
      * 返回值 pairRdd
      *     1. 将每个分区内key相同数据放到一起 （cat,(2,5,100)）100是初始值也会被加入
      *     2.对每个分区的每个key进行聚合( 发现好像不是那么好用),这个seqOp 是reduce的计算模式,而且reduce的初始值就是aggregateByKey的初始值
      *     3.合并每个分区的每个key（注意被合并的是pair类型）
      */
    def aggregateByKey_op(): Unit = {
        val data = List(("cat", 2), ("cat", 4), ("mouse", 4), ("cat", 12), ("dog", 12), ("mouse", 2))
        /*这个分区的结果不是确定的，而是变化的*/
        val rdd = sc.parallelize(data, 2)
        val result = rdd.aggregateByKey(0)(math.max(_, _), _ + _).collect
        result.foreach(println(_))
    }

    def aggregateByKey_op2(): Unit = {
        val data = List((1, 3), (1, 2), (1, 4), (2, 3), (1, 5), (1, 6))
        val rdd = sc.parallelize(data, 2)

        //合并不同partition中的值，a，b得数据类型为zeroValue的数据类型
        def combOp(a: String, b: String): String = {
            println("combOp: " + a + "\t" + b)
            a + b
        }

        //合并在同一个partition中的值，a的数据类型为zeroValue的数据类型，b的数据类型为原value的数据类型
        def seqOp(a: String, b: Int): String = {
            println("SeqOp:" + a + "\t" + b)
            a + b
        }

        rdd.foreachPartition(part => {
            part.foreach(x => print(x + "\t"));
            println("==========================================")
        })
        //zeroValue:中立值,定义返回value的类型，并参与运算
        //seqOp:用来在同一个partition中合并值
        //combOp:用来在不同partiton中合并值
        val aggregateByKeyRDD = rdd.aggregateByKey("100")(seqOp, combOp)
        aggregateByKeyRDD.foreach(println(_))
    }

    /**
      * aggregateByKey 达到 reduceByKey的效果，完成wc
      */
    def aggregateByKey_WC(): Unit = {
        val initialScores = Array(("Fred", 2), ("Fred", 1), ("Fred", 3), ("Wilma", 2), ("Wilma", 5), ("Wilma", 3))
        val d1 = sc.parallelize(initialScores, 2)

        def sum(int1: Int, int2: Int) = int1 + int2

        def com_sum(int1: Int, int2: Int) = int1 + int2

        val result = d1.aggregateByKey(0)(
            sum(_, _),
            com_sum(_, _)
        )
        result.foreach(println(_))
    }

    /**
      * mapPartitions 函 数 获 取 到 每 个 分 区 的 迭 代器，在 函 数 中 通 过 这 个 分 区 整 体 的 迭 代 器 对整 个 分 区 的 元 素 进 行 操 作
      * 有点类似 foreachPartition 只不过 foreachPartition 没有返回值
      */
    def map_partition_op(): Unit = {
        val initialScores = Array(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20)
        val d1 = sc.parallelize(initialScores, 2)
        show_parttion_int(d1)

        def deal_partition(iterator: Iterator[Int]): Iterator[Int] = {
            iterator.filter(_ % 2 == 0)
        }

        val d2 = d1.mapPartitions(deal_partition)
        show_parttion_int(d2)
    }

    /**
      * 和 mapPartitions操作类似，只不过在操作的时候提供了partition的下标供操作
      *     可以看出rdd分区的下标是从零开始的
      */
    def mapPartitionsWithIndex_op(): Unit ={
        val initialScores = Array(1, 2, 3, 4, 5, 6, 1, 1, 1, 1, 2, 3, 4, 3, 2, 3, 2, 3, 4, 2, 4, 5)
        val d1 = sc.parallelize(initialScores, 2)
        def deal_PartitionsWithIndex(index: Int,part: Iterator[Int]): Iterator[String]={
            part.map(x=>"[partID:" +  index + ", value: " + x + "]")
        }
        val result=d1.mapPartitionsWithIndex(deal_PartitionsWithIndex)
        result.foreach(println(_))
    }


    def union_op(): Unit = {
        val initialScores = Array(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20)
        val d1 = sc.parallelize(initialScores, 2)
        val d2 = sc.parallelize(initialScores, 2)
        //  ++ 操作 就是 union
        val result = d1 ++ d2
        show_parttion_int(result)
    }

    /**
      * 与 groupby_key 相比，需要一个函数，将值生成对应的key，然后按key 分组
      */
    def groupBy_op(): Unit = {
        val initialScores = Array(1, 2, 3, 4, 5, 6, 1, 1, 1, 1, 2, 3, 4, 3, 2, 3, 2, 3, 4, 2, 4, 5)
        val d1 = sc.parallelize(initialScores, 2)
        val result = d1.groupBy(k => {
            if (k % 2 == 0) 1 else 0
        })
        show_parttion_int_itrator(result)
    }

    /**
      * 提供一个函数为每个元素生成一个key,但是并没有按key 去分组，只是形成了一个key-value 型的Rdd ,表明每个元素的key
      */
    def keyBy_op(): Unit ={
        val rdd=sc.parallelize(Array(1,2,3,4,5,6,7,8))
        rdd.groupBy(_%2).foreach(println(_))
        rdd.keyBy(_%2).foreach(println(_))
    }

    /**
      * mapValues ：针对（Key， Value）型数据中的 Value 进行 Map 操作，而不对 Key 进行处理。而map操作在pairRdd中是对键值对的操作
      */
    def mapValues_op(): Unit = {
        val initialScores = Array(("a", 1), ("b", 2))
        val d1 = sc.parallelize(initialScores)
        val result = d1.mapValues(v => v + 2)
        show_parttion_pair(result)

    }

    /**
      * flatMapValues 针对 value 进行 flatmap 操作
      */
    def flatMapValues_op(): Unit = {
        val initialScores = Array(("a", "1,2,3"), ("b", "4,5,6"))
        val d1 = sc.parallelize(initialScores)
        val result = d1.flatMapValues(_.split(",")).mapValues(_.toInt)
        show_parttion_pair(result)
    }



    /**
      * 　subtract相当于进行集合的差操作,减去在另一个rdd中存在的元素
      */
    def subtract_op(): Unit = {
        val initialScores = Array(1, 2, 3, 4, 5, 6, 7, 8)
        val d1 = sc.parallelize(initialScores)
        val initialScores2 = Array(1, 2, 3, 4, 5)
        val d2 = sc.parallelize(initialScores2)
        val result = d1.subtract(d2)
        show_parttion_int(result)
    }

    /**
      * subtractByKey 针对pairRdd 根据key 进行求差
      */
    def subtractByKey_op(): Unit = {
        val initialScores = Array(("a",1),("b",2))
        val d1 = sc.parallelize(initialScores)
        val initialScores2 = Array(("a",2),("c",3))
        val d2 = sc.parallelize(initialScores2)
        val result = d1.subtractByKey(d2)
        show_parttion_pair(result)
    }

    /**
      * 不止要求分区数目相等，而且要求每个分区的数据条数相等
      */
    def zip_op(): Unit ={
        val rdd=sc.parallelize(Array(1,2,3,4,5,6,7,8))
        val rdd2=sc.parallelize(Array("a","b","c","d","e","f","g","i"))
        rdd.zip(rdd2).foreach(println(_))

    }

    /**
      *  partitioner 获取的是一个option 对象
      *  partitions 获取到的是一个Array[Partition]
      *  spark 提供了两种分区 HashPartitioner   RangePartitioner
      *
      *  注意：
      *     1. 这个算子对于某些操作可能大幅度提高性能
      *     2. 但是正确的使用节奏是分区之后的结果应该缓存，不然后面的每个action 操作都会重新分区
      *     3. 只对静态RDD 做这个操作
      *
      */
    def partitionBy_op(): Unit ={
        val initialScores = Array(("a",1),("b",2),("c",3),("d",4),("e",5),("f",6))
        val nopartition=sc.parallelize(initialScores)
        println(nopartition.partitioner)
        val partition=nopartition.partitionBy(new HashPartitioner(3)).persist
        println(partition.partitioner)
        println(partition.partitions.length)
        println(partition.partitioner.get.numPartitions)
        println("获取各个分区================================")
        println(partition.partitioner.get.getPartition(0))
        println(partition.partitioner.get.getPartition(1))
        println(partition.partitioner.get.getPartition(2))
        // 下面会获取到分区0
        println(partition.partitioner.get.getPartition(3))
    }

    /**
      * 自定义分区操作
      */
    def definePpartition(): Unit ={
        /**
          * 自定义分区 根据key 的最后一位数字决定元素进入那个分区
          * @param numParts
          */
        class mypartitoner(numParts: Int) extends Partitioner {
            // 要创建的分区个数
            override def numPartitions: Int = numParts
            // 返回给定键的分区编号（也就是根据这个函数来确定元素的分区）
            override def getPartition(key: Any): Int = key.toString.toInt % 10
        }
        val data=sc.parallelize(1 to 10,5)
        //根据尾号转变为10个分区，分写到10个文件（首先变成了pairRdd）
        data.map((_,1)).partitionBy(new mypartitoner(10)).saveAsSequenceFile("chenm/partition2")
    }

    /**
      * 改变分区的操作
      *     coalesce 可以在没有shuffle的情况下改变分区大小，但是此时只能是合并分区
      */
    def changePartition(): Unit ={
        val rdd=sc.parallelize((1 to 100).toList,10)
        println(rdd.partitions.length)
        // coalesce 在第二个参数为false 的情况下（没有shuffle）是不能增加分区个数的
        val rdd2=rdd.coalesce(20,false)
        println(rdd2.partitions.length)
        // repartition 分区可大可小
        val rdd3=rdd.repartition(5)
        println(rdd3.partitions.length)
    }

    /**
      * 返回结果是一个数组
      */
    def take_op(): Unit ={
        val initialScores = Array(1, 1, 1, 1, 1, 2, 3, 4, 5, 6, 7, 8,3)
        val d1 = sc.parallelize(initialScores)
        val result = d1.take(3)
        result.foreach(println(_))
    }



    /**
      * 抽样操作
      * 是否放回,抽样比例,种子数
      */
    def sample_op(): Unit = {
        val initialScores = Array(6, 5, 1, 7, 9, 2, 3, 4, 5, 6, 7, 8)
        val d1 = sc.parallelize(initialScores)
        val result = d1.sample(false, 0.5, 9)
        show_parttion_int(result)
    }

    /**
      * 　takeSample（）函数和上面的sample函数是一个原理，但是不使用相对比例采样，而是按设定的采样个数进行采样，同时返回结果不再是RDD，而是相当于对采样后的数据进行
      * Collect（），返回结果的集合为单机的数组(这个可以理解，按比例抽取的数据量可能很大，所以是分布式的，但是按个数抽取的量一定不大)
      */
    def take_sample_op(): Unit = {
        val initialScores = Array(1, 1, 1, 1, 1, 2, 3, 4, 5, 6, 7, 8)
        val d1 = sc.parallelize(initialScores)
        val result = d1.takeSample(false, 5, 9)
        println(result.mkString("\t"))
    }

    /**
      * 该算子和collect类似，只不过对（K，V）型的RDD数据返回一个单机HashMap。 对于重复K的RDD元素，后面的元素覆盖前面的元素。
      */
    def collectAsMap(): Unit = {
        val initialScores = Array(("Fred", 2), ("Fred", 1), ("Fred", 3), ("Wilma", 2), ("Wilma", 5), ("Wilma", 3))
        val d1 = sc.parallelize(initialScores)
        val result = d1.collectAsMap()
        result.foreach(println(_))
    }

    /**
      * 实现的是先reduce再collectAsMap的功能，先对RDD的整体进行reduce操作，然后再收集所有结果返回为一个HashMap
      * 从 Locally 可以判断出有 collect 的功能，从返回的类型 可以判断是 collectAsMap
      */
    def reduceByKeyLocally(): Unit = {
        val initialScores = Array(("Fred", 2), ("Fred", 1), ("Fred", 3), ("Wilma", 2), ("Wilma", 5), ("Wilma", 3))
        val d1 = sc.parallelize(initialScores)
        val result = d1.reduceByKeyLocally(_ + _)
        result.foreach(println(_))

    }

    /**
      * Lookup函数对（Key，Value）型的RDD操作，返回指定Key对应的元素形成的Seq
      */
    def lookup(): Unit = {
        val initialScores = Array(("Fred", 2), ("Fred", 1), ("Fred", 3), ("Wilma", 2), ("Wilma", 5), ("Wilma", 3))
        val d1 = sc.parallelize(initialScores)
        val result = d1.lookup("Fred")
        println(result.mkString("\t"))
    }

    /**
      * top可返回最大的k个元素
      * .top返回最大的k个元素。
      * ·take返回最小的k个元素。
      * ·takeOrdered返回最小的k个元素，并且在返回的数组中保持元素的顺序。
      * ·first相当于top（1）返回整个RDD中的前k个元素，可以定义排序的方式Ordering[T]。
      */
    def top_op(): Unit = {
        val initialScores = Array(1, 1, 1, 1, 1, 2, 3, 4, 5, 6, 7, 8)
        val d1 = sc.parallelize(initialScores)
        val result = d1.top(2)
        println(result.mkString("\t"))
        println()
    }

    /**
      * 这是一个高级操作，使用场景并不是很多
      */
    def pipe_op(): Unit ={
        val initialScores = Array(1, 1, 1, 1, 1, 2, 3, 4, 5, 6, 7, 8)
        val rdd = sc.parallelize(initialScores)
        val se="./doc/se.sh"
        sc.addFile(se)
        val result=rdd.pipe(Seq(SparkFiles.get(se)))
        result.foreach(println(_))
    }

    /**
      * 缓存操作
      */
    def cache_Op(): Unit = {
        val rdd = sc.parallelize(Array(("a", 1), ("b", 2), ("c", 3)))
        /**
          * 堆外存储，在这种情况下，序列化的RDD将保存在Tachyon的堆外存储上，这个选项有很多好处，最重要的一个好处是可以在executor及其他应用之间共享一个内存池，减少垃圾回收带来的消耗
          */
        rdd.persist(StorageLevel.OFF_HEAP)
        import ss.implicits._
        val df = rdd.toDF
        df.createOrReplaceTempView("tab_name")

        /**
          * SparkSQL缓存的表是以列式存储在内存中的，使得在做查询时，不需要对整个数据集进行全部扫描，仅需要对需要的列进行扫描，所以性能有很大提升。
          * 如果数据是以列式存储的，SparkSQL就能按列自动选择最优的压缩编码器，对它调优以减少内存使用及垃圾回收压力。
          * DataFrame本身也是rdd，所以其实也可以直接按rdd的缓存方式缓存DataFrame
          */
        ss.sqlContext.cacheTable("tab_name")
        // 和上面的缓存效果是一样的
        ss.sql("CACHE TABLE tab_name")
    }

    /**
      *  RDD 自定义排序操作
      *       所有的排序都是借助key 完成的，
      *       对于非排序类型的算子来讲，分区采用的是散列算法分区的，只需要保证相同的key被分配到相同的partition中就可以，并不会影响其他计算操作，而排序操作是借助Range进行分区的
      *      sortBy
      *           底层也是借助sortByKey进行操作的，所以它需要一个函数来生成key
      *      sortByKey  PairRdd
      *         　第一个参数是ascending，参数决定排序后RDD中的元素是升序还是降序，默认是true，也就是升序；
      *           第二个参数是numPartitions，该参数决定排序后的RDD的分区个数，默认排序后的分区个数和排序之前的个数相等，即为this.partitions.size。
      *
      *           特殊时候，你需要自定义key 对象，完成自定义排序
      */
    def sort_op(): Unit ={
        val gradeRdd =  sc.parallelize(List(("004",90,70,96),("002",87,76,89),("001",90,56,87),("003",82,78,76)))
        //将rdd中的数据转为key-value的形式，使用sortByKey进行排序(根据 key 进行排序)
        val arrStudent = gradeRdd.map(s => (s._1.toInt,s)).sortByKey().foreach(println(_))

        val gradeRdd2 =  sc.parallelize(List(("004",90,70,96),("002",87,76,89),("001",90,56,87),("003",82,78,76)))
        val arrStudent2 = gradeRdd.sortBy(s=>s._1.toInt).foreach(println(_))
        /**
          * 根据美女的颜值与年龄进行排序，如果颜值相等，年龄小的排在前面
          * @param faceValue
          * @param age
          */
        case class Girl( faceValue: Int, age: Int) extends Ordered[Girl] with Serializable {
            override def compare(that: Girl): Int = {
                if (this.faceValue == that.faceValue) {
                    that.age - this.age
                } else {
                    this.faceValue - that.faceValue
                }
            }
        }
        val rdd1 = sc.parallelize(List(("yuihatan", 90, 28, 1), ("angelababy", 90, 27, 2), ("JuJingYi", 95, 22, 3)))
        val rdd2 = rdd1.sortBy(x => Girl(x._2, x._3), false)
        println(rdd2.collect.toBuffer)

    }

    /**
      * partitions 方法将RDD-> Array[Partition]
      * glom 方法将 RDD->RDD[Array]  其中每个分区是一个数组
      */
    def glom_op(): Unit ={
        val initialScores = Array(1, 1, 1, 1, 1, 2, 3, 4, 5, 6, 7, 8)
        val d1 = sc.parallelize(initialScores,5)
        println(d1.partitions.length)
        val d2=d1.glom()
        d2.foreach(x=>println(x.length))
    }


    /**
      * 将 RDD 以分区的形式展示出来
      */
    def show_parttion_int(rdd: RDD[Int]): Unit = {
        rdd.foreachPartition(part => {
            println("=================================================================================")
            part.foreach(x => {
                print(x + "\t")
            })
            println("=================================================================================")
        })
    }

    def show_parttion_pair(rdd: RDD[(String, Int)]): Unit = {
        rdd.foreachPartition(part => {
            println("=================================================================================")
            part.foreach(x => {
                print(x + "\t")
            })
            println("=================================================================================")
        })
    }

    def show_parttion_int_itrator(rdd: RDD[(Int, Iterable[Int])]): Unit = {
        rdd.foreachPartition(part => {
            println("=================================================================================")
            part.foreach(x => {
                print(x + "\t")
            })
            println("=================================================================================")
        })
    }




}

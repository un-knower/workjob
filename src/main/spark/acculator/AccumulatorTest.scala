package acculator

import org.apache.spark.rdd.RDD
import org.apache.spark.util.{CollectionAccumulator, DoubleAccumulator, LongAccumulator}
import org.apache.spark.{SparkConf, SparkContext}

/**
  * spark 目前有三个自带的累加器
  *     1.CollectionAccumulator
  *     2.DoubleAccumulator
  *     3.LongAccumulator
  *  自定义累加器
  *     1. 继承  AccumulatorV2[IN, OUT]  抽象类，实现其中的方法
  *  意义：
  *     将结果返回到driver端
  *  容错性（错误使用方式）
  *     1.累加器不会改变spark的lazy的计算模型,所以必须要借助（或者直接在action算子中使用）action 算子才能使累加器生效
  *     2.要想任务重启（执行失败或者执行很慢）累加器的结果也要可靠，必须放在行动算子中
  */
object AccumulatorTest {

    def main(args: Array[String]): Unit = {
        val conf = new SparkConf().setAppName("Accumulator1").setMaster("local[2]")
        val sc = new SparkContext(conf)
        errorUseAaccurator2(sc)
    }

    def getlongAccumulator(sc: SparkContext, name: String): LongAccumulator = {
        val acc = new LongAccumulator
        sc.register(acc, name)
        acc
    }

    /**
      * 缺少action，导致累加器没有被执行
      * @param sc
      */
    def errorUseAaccurator(sc: SparkContext)={
        val acc=getlongAccumulator(sc,"ACC")
        val rdd:RDD[Int] =sc.makeRDD[Int](List(1,2,3,4,5,6,7,8,9,10))
        rdd.map(line=>{
            if(line % 2==0){
                println("累加器被加一次")
                acc.add(1L)
            }
        })
        println(s"累加器的值是：${acc.value}")
        sc.stop()
    }

    /**
      * 由于依赖的关系导致 rdd 被算两次——累加器也被累加两次，所以在这里先进行了缓存
      * 但是缓存不是万能的：
      *     缓存在长时间没有被使用的情况下会被移除内存，此时要是再使用就会根据系谱图重算，此时累加器就会被多次调用，导致计算结果出错
      * @param sc
      */
    def errorUseAaccurator2(sc: SparkContext)={
        val acc=getlongAccumulator(sc,"ACC")
        val rdd:RDD[Int] =sc.makeRDD[Int](List(1,2,3,4,5,6,7,8,9,10))
        val result=rdd.map(line=>{
            if(line % 2==0){
                println("累加器被加一次")
                acc.add(1L)
            }
            line
        })
        val res:RDD[Int]=result.cache()
        val sum=res.reduce(_+_)
        val count=res.count()
        println(s"累加器的值是：${acc.value}")
        sc.stop()
    }
    def rightUseAaccurator(sc: SparkContext)={
        val acc=getlongAccumulator(sc,"ACC")
        val rdd:RDD[Int] =sc.makeRDD[Int](List(1,2,3,4,5,6,7,8,9,10))
        rdd.map(line=>{
            if(line % 2==0){
                println("累加器被加一次")
                acc.add(1L)
            }
            line
        }).reduce(_+_)
        println(s"累加器的值是：${acc.value}")
        sc.stop()
    }

    def doubleAccumulator(sc: SparkContext, name: String): DoubleAccumulator = {
        val acc = new DoubleAccumulator
        sc.register(acc, name)
        acc
    }

    /**
      * 获取spark 自带的累加器
      * @param sc
      * @param name
      * @tparam T
      * @return
      */
    def getcollectionAccumulator[T](sc: SparkContext, name: String): CollectionAccumulator[T] = {
        val acc = new CollectionAccumulator[T]
        sc.register(acc, name)
        acc
    }

    def getmydefineAccumulator(sc: SparkContext, name: String): MySetAccumulator = {
        val acc = new MySetAccumulator()
        sc.register(acc, name) // Accumulator must be registered before send to executor
        acc
    }

    def testMySetAccumulator(sc: SparkContext): Unit = {
        val nums = Array("1", "2", "3", "4", "5", "6", "7", "8", "5", "5")
        val numsRdd = sc.parallelize(nums)
        val myAcc: MySetAccumulator = getmydefineAccumulator(sc, "myAcc")
        numsRdd.foreach(num => {
            if (myAcc.myadd(num)) {
                println(num)
            }
            else {
                println("添加失败")
            }
        })
        println(myAcc.value)
        sc.stop()
    }

    /**
      * 注册累加器
      * @param sc
      * @param name
      * @return
      */
    def getMyMapAccumulator(sc: SparkContext, name: String): MyMapAccumulator = {
        val acc = new MyMapAccumulator()
        // Accumulator must be registered before send to executor
        sc.register(acc, name)
        acc
    }

    def testMyMapAccumulator(sc: SparkContext): Unit = {
        val myAcc: MyMapAccumulator = getMyMapAccumulator(sc, "myAcc")
        val tmprdd = sc.textFile("src/main/resources/externalfile/wc.txt")
        val wcrdd = tmprdd.flatMap(_.split(" ")) map (x => (x, 1))
        wcrdd.foreach(num => {
            myAcc.myadd(num)
        })
        println(myAcc.value)
        sc.stop()
    }

    /**
      *  下面的这种是spark 自带的累加器，虽然这里的集合用的是list ，但是多写两个判断，同样可以完成set 的需求 自定义set累加器的功能
      * @param sc
      */
    def testcollectionAccumulator(sc: SparkContext): Unit = {
        val nums = Array("1", "2", "3", "4", "5", "6", "7", "8", "5", "5")
        val numsRdd = sc.parallelize(nums)
        val myAcc: CollectionAccumulator[String] = getcollectionAccumulator(sc, "myAcc")
        numsRdd.foreach(num => {
            if (myAcc.value.contains(num)) {
                println(num + " 已结被包含")
            }
            else {
                println(num)
                myAcc.add(num)
            }
        })
        println(myAcc.value)
        sc.stop()
    }

}

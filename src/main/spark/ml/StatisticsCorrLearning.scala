package sparkdemo.ml

import org.apache.spark.mllib.stat.Statistics
import org.apache.spark.{SparkConf, SparkContext}

object StatisticsCorrLearning {
    def main(args: Array[String]) {
        val conf = new SparkConf().setMaster("spark://ts:7077").setAppName(this.getClass().getSimpleName().filter(!_.equals('$')))
        val sc = new SparkContext(conf)

        val rddX = sc.textFile("hdfs://ts:9000/usr/workingsoft/spark-2.0.2/data/mllib/input/basic/StatisticsCorrLearningx.txt") //读取数据
                .flatMap(_.split(' ') //进行分割
                .map(_.toDouble)) //转化为Double类型
        val rddY = sc.textFile("file/data/mllib/input/basic/StatisticsCorrLearningy.txt") //读取数据
                .flatMap(_.split(' ') //进行分割
                .map(_.toDouble)) //转化为Double类型
        println("rddX:")
        rddX.foreach(each => print(each + " "))
        println("\nrddY:")
        rddY.foreach(each => print(each + " "))
        var correlationPearson: Double = Statistics.corr(rddX, rddY) //计算不同数据之间的相关系数:皮尔逊
        println("\ncorrelationPearson：" + correlationPearson) //打印结果

        var correlationSpearman: Double = Statistics.corr(rddX, rddY, "spearman") //使用斯皮尔曼计算不同数据之间的相关系数
        println("correlationSpearman：" + correlationSpearman) //打印结果

        //不读取文件直接生成
        println("\nSecond:")
        val arr1 = (1 to 10).toArray.map(_.toDouble)
        val arr2 = (1 to 10).toArray.map(_.toDouble)
        val rdd1 = sc.parallelize(arr1)
        val rdd2 = sc.parallelize(arr2)
        println("rdd1:")
        rdd1.foreach(each => print(each + " "))
        println("\nrdd2:")
        rdd2.foreach(each => print(each + " "))
        correlationPearson = Statistics.corr(rdd1, rdd2) //计算不同数据之间的相关系数:皮尔逊
        println("\ncorrelationPearson：" + correlationPearson) //打印结果
        correlationSpearman = Statistics.corr(rdd1, rdd2, "spearman") //使用斯皮尔曼计算不同数据之间的相关系数
        println("correlationSpearman：" + correlationSpearman) //打印结果

        println("\nThird:")
        val arr3 = (1 to 5).toArray.map(_.toDouble)
        val arr4 = (2 to 10 by 2).toArray.map(_.toDouble)
        val rdd3 = sc.parallelize(arr3)
        val rdd4 = sc.parallelize(arr4)
        println("rdd3:")
        rdd3.foreach(each => print(each + " "))
        println("\nrdd4:")
        rdd4.foreach(each => print(each + " "))
        val correlationPearson3 = Statistics.corr(rdd3, rdd4) //计算不同数据之间的相关系数:皮尔逊
        println("\ncorrelationPearson3：" + correlationPearson3) //打印结果

        val correlationSpearman3 = Statistics.corr(rdd3, rdd4, "spearman") //使用斯皮尔曼计算不同数据之间的相关系数
        println("correlationSpearman3：" + correlationSpearman3) //打印结果

        println("\nFourth:")
        val rdd5 = sc.parallelize(Array(5.0, 3.0, 2.5))
        val rdd6 = sc.parallelize(Array(4.0, 3.0, 2.0))
        println("rdd5:")
        rdd5.foreach(each => print(each + " "))
        println("\nrdd6:")
        rdd6.foreach(each => print(each + " "))
        val correlationPearson5 = Statistics.corr(rdd5, rdd6) //计算不同数据之间的相关系数:皮尔逊
        println("\ncorrelationPearson5：" + correlationPearson5) //打印结果
        //与链接和书计算结果一致：http://blog.csdn.net/dc_726/article/details/40017997 书：ref

        val correlationSpearman5 = Statistics.corr(rdd5, rdd6, "spearman") //使用斯皮尔曼计算不同数据之间的相关系数
        println("correlationSpearman5：" + correlationSpearman5) //打印结果

        println("\nFifth:")
        val rdd7 = sc.parallelize(Array(170.0, 150.0, 210.0, 180.0, 160.0))
        val rdd8 = sc.parallelize(Array(180.0, 165.0, 190.0, 168.0, 172.0))
        println("rdd:")
        rdd7.foreach(each => print(each + " "))
        println("\nrdd:")
        rdd8.foreach(each => print(each + " "))
        val correlationPearson7 = Statistics.corr(rdd7, rdd8) //计算不同数据之间的相关系数:皮尔逊
        println("\ncorrelationPearson：" + correlationPearson7) //打印结果
        //与链接和书计算结果一致：http://baike.baidu.com/link?url=s9aiihE4mMpF2sZLqR33JKz3FNk8R8IWWh9-ZgNFO4aZB5ez9mnADNQZQSApniWXUJGwhr-Ar9mjWEFVwncQlq书：ref

        val correlationSpearman7 = Statistics.corr(rdd7, rdd8, "spearman") //使用斯皮尔曼计算不同数据之间的相关系数
        println("correlationSpearman：" + correlationSpearman7) //打印结果


        sc.stop
    }

}

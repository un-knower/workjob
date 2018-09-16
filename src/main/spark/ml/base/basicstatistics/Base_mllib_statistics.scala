package sparkDM.ML.base.Basicstatistics

import org.apache.spark.mllib.linalg.Vectors
import org.apache.spark.mllib.linalg.distributed.{CoordinateMatrix, RowMatrix}
import org.apache.spark.{SparkConf, SparkContext}
import org.apache.spark.mllib.stat.{MultivariateStatisticalSummary, Statistics}
import org.apache.spark.sql.{Row, SparkSession}


object Base_mllib_statistics {
    val sparkSession = SparkSession.builder().master("local[2]").appName("test").getOrCreate()
    val sc = sparkSession.sparkContext

    import sparkSession.sqlContext.implicits._

    def main(args: Array[String]): Unit = {
        base_correction2()
    }

    def mean_standDifferebce(): Unit = {
        var rdd = sc.textFile("src/main/resources/learning/kimi.txt")
                .map(_.split(' ')
                        .map(_.toDouble))
                .map(line => Vectors.dense(line));
        println("rdd count:" + rdd.count())
        var summary = Statistics.colStats(rdd); //以列为基础计算统计量的基本数据，也就是说计算完返回一个对象里面包含很多信息
        println(summary)
        println(summary.mean); //计算均值
        println(summary.variance); //计算标准差
        println(summary.max)
        println(summary.min)
        println(summary.count)
        println(summary.normL1)
        println(summary.normL2)
    }

    def base_correction(): Unit = {
        val sc = sparkSession.sparkContext;
        val rddX = sc.textFile("src/main/resources/learning/x.txt").flatMap(_.split(' ').map(_.toDouble))
        val rddY = sc.textFile("src/main/resources/learning/y.txt").flatMap(_.split(' ').map(_.toDouble))
        //皮尔逊相关系数 1.0
        var correlation: Double = Statistics.corr(rddX, rddY)
        println(correlation)
        //斯皮尔曼相关系数 1.0000000000000009
        val correlation2: Double = Statistics.corr(rddX, rddY, "spearman");
        println(correlation2);
        //单个数据集相关系数的计算 自己与自己的相关系数
        println(Statistics.corr(rddX.map(line => Vectors.dense(line))))

    }

    def base_correction2(): Unit = {
        val rdd1 = sc.parallelize(
            Array(
                Array(1.0, 2.0, 3.0, 4.0),
                Array(2.0, 3.0, 4.0, 5.0),
                Array(3.0, 4.0, 5.0, 6.0)
            )
        ).map(f => Vectors.dense(f))
        //创建RowMatrix
        val rowMatirx = new RowMatrix(rdd1)
        var coordinateMatrix: CoordinateMatrix = rowMatirx.columnSimilarities()
        coordinateMatrix.entries.collect().map(println(_))
        /*计算列统计信息*/
        var mss: MultivariateStatisticalSummary = rowMatirx.computeColumnSummaryStatistics()
        println(mss.min)
        println(mss.max)
        println(mss.mean)
        println(mss.numNonzeros)
        println(mss.variance)

    }
}

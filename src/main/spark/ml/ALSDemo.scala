package sparkdemo.ml

import org.apache.spark.SparkContext
import org.apache.spark.mllib.recommendation.{ALS, MatrixFactorizationModel, Rating}
import org.apache.spark.sql.SparkSession

/**
  * Spark MLlib协同过滤算法示例
  * 引用自官网：http://spark.apache.org/docs/latest/mllib-collaborative-filtering.html
  * created by Trigl at 2017-05-09 17:40
  */
object ALSDemo {
    System.setProperty("spark.serializer", "org.apache.spark.serializer.KryoSerializer")
    val spark = SparkSession.builder().master("local").appName("spark_mllib").config("spark.sql.warehouse.dir", "src/main/resources/warehouse").getOrCreate()
    val sc = spark.sparkContext

    def main(args: Array[String]): Unit = {

    }

    /**
      * 如何使用已有的模型去做推荐
      *
      * @param sc
      */
    def useMode(sc: SparkContext): Unit = {
        val myModelPath = "src/main/resources/hadoop/model"
        val sameModel = MatrixFactorizationModel.load(sc, myModelPath)

    }

    def tarinMode(sc: SparkContext) {
        val data = sc.textFile("src/main/resources/learning/test.data")
        val ratings = data.map(_.split(',') match {
            case Array(user, item, rate) =>
                Rating(user.toInt, item.toInt, rate.toDouble) // 注意用户和物品必须是整数，评分是双精度浮点数
        })

        // 使用ALS建立推荐模型  ALS算法是2008年以来，用的比较多的协同过滤算法。它已经集成到Spark的Mllib库中，使用起来比较方便。从协同过滤的分类来说，ALS算法属于User-Item CF，
        //但它却同时考虑了它同时考虑了User和Item两个方面。
        val rank = 10 // 最后推荐的物品数
        val numIterations = 10 // 迭代次数
        val model = ALS.train(ratings, rank, numIterations, 0.01)

        // 测试集 ，依然是一个RDD
        val usersProducts = ratings.map { case Rating(user, product, rate) =>
            (user, product)
        }

        // 预测集 依然是一个RDD(Rating(uer,item,rate))==   转换成   ==>RDD((uer,item),rate)
        val predictions =
            model.predict(usersProducts).map { case Rating(user, product, rate) =>
                ((user, product), rate)
            }

        // 实际评分和预测评分集
        // RDD Join 类似于 SQL的inner join操作，返回结果是前面和后面集合中配对成功的，过滤掉关联不上的,而且只保存了一份条件字段
        val ratesAndPreds = ratings.map { case Rating(user, product, rate) =>
            ((user, product), rate)
        }.join(predictions)

        val MSE = ratesAndPreds.map { case ((user, product), (r1, r2)) =>
            val err = (r1 - r2)
            err * err
        }.mean()
        println("Mean Squared Error = " + MSE)

        // 保存和加载模型
        val myModelPath = "src/main/resources/hadoop/model"
        model.save(sc, myModelPath)
    }


}

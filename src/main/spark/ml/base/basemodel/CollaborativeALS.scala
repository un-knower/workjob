package ml.base.basemodel

import org.apache.spark.mllib.recommendation.{ALS, MatrixFactorizationModel, Rating}
import org.apache.spark.sql.SparkSession

/**
  * Spark中协同过滤算法主要由交替最小二乘法来实现 alternating least squares (ALS)
  */
object CollaborativeALS {
    val sparkSession = SparkSession.builder().master("local[2]").appName("test").getOrCreate()
    val sc = sparkSession.sparkContext

    def main(args: Array[String]): Unit = {
        recomend()
    }
    def recomend(): Unit ={
        val data = sc.textFile("data/mllib/als/test.data")
        val ratings = data.map(_.split(',') match { case Array(user, item, rate) =>
            Rating(user.toInt, item.toInt, rate.toDouble)
        })
        /**
          * - rank：对应的是隐因子的个数，这个值设置越高越准，但是也会产生更多的计算量。一般将这个值设置为10-200；  也就是特征个数
          * - iterations：对应迭代次数，一般设置个10就够了；
          * - lambda：该参数控制正则化过程，其值越高，正则化程度就越深。一般设置为0.01。
          */
        val rank = 10
        val numIterations = 10
        val model = ALS.train(ratings, rank, numIterations, 0.01)

        val usersProducts = ratings.map { case Rating(user, product, rate) =>
            (user, product)
        }
        val predictions =
            model.predict(usersProducts).map { case Rating(user, product, rate) =>
                ((user, product), rate)
            }
        val ratesAndPreds = ratings.map { case Rating(user, product, rate) =>
            ((user, product), rate)
        }.join(predictions)

        val MSE = ratesAndPreds.map { case ((user, product), (r1, r2)) =>
            val err = (r1 - r2)
            err * err
        }.mean()

        //均方误差
        println("Mean Squared Error = " + MSE)

//        model.save(sc, "target/tmp/myCollaborativeFilter")
//        val sameModel = MatrixFactorizationModel.load(sc, "target/tmp/myCollaborativeFilter")

    }

}

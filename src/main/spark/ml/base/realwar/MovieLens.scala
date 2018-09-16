package ml.base.realwar

import org.apache.spark.mllib.recommendation.{ALS, Rating}
import org.apache.spark.sql.SparkSession

object MovieLens {
    val sparkSession = SparkSession.builder().master("local[2]").appName("test").getOrCreate()
    val sc = sparkSession.sparkContext

    def main(args: Array[String]): Unit = {
        recommend()
    }

    /**
      * 使用moivelens的数据进行推荐
      */
    def recommend(): Unit = {
        val rate_data = sc.textFile("data/mllib/als/movielens/data.txt").map(_.split("\t").take(3))
        val ratings = rate_data.map { case Array(user, movie, rating) => Rating(user.toInt, movie.toInt, rating.toDouble) }
        /**
          * 模型对象主要包括两个对象
          *   model.productFeatures
          *   model.userFeatures
          */
        val model = ALS.train(ratings, 50, 10, 0.01)
        // 使用模型  某个用户对某个物品的评分
       println(model.predict(789, 123))
        // 为用户789推荐前10个物品：
        model.recommendProducts(789,10).foreach(println(_))
        println("======================================")
        // 为某个物品推荐10 个用户
        model.recommendUsers(123,10)foreach(println(_))
    }

    /**
      * 探索评分数据   用户id | 影片id | 评分值 | 时间戳(timestamp格式)
      */
    def explodeDataRating(): Unit = {
        val rate_data = sc.textFile("data/mllib/als/movielens/data.txt").map(_.split("\t"))
        println(rate_data.first())
        println(rate_data.count())
        val ratings = rate_data.map(x => x(2).toInt).cache()
        println(ratings.max())
        println(ratings.min())
        println(ratings.mean())
        println(ratings.variance())
        ratings.countByValue().foreach(println(_))
    }

    /**
      * 影片id | 影片名 | 影片发行日期 | 影片链接 | ...
      */
    def explodeDataMovie(): Unit = {
        val movie_data = sc.textFile("data/mllib/als/movielens/item.txt")
        val movie_fields = movie_data.map(_.split("\\|"))
        println(movie_fields.count())
        val filtermovie = movie_fields.filter(fileds => {
            try {
                val time = fileds(2).split("-")(2).toInt
                true
            } catch {
                case e: Exception => false
            }
        }).cache()
        val movieyear = filtermovie.map(x => x(2).split("-")(2).toInt)
    }

    /**
      * 用户id | 用户年龄 | 用户性别 | 用户职业 | 用户邮政编码
      */
    def explodeDataUser(): Unit = {
        val user_data = sc.textFile("data/mllib/als/movielens/user.txt")
        val user_fields = user_data.map(_.split("\\|")).cache()
        val num_users = user_fields.map(fields => fields(0)).count()
        val num_genders = user_fields.map(fields => fields(2)).distinct().count()
        val num_occupations = user_fields.map(fields => fields(3)).distinct().count()
        val num_zipcodes = user_fields.map(fields => fields(4)).distinct().count()
        val occupations_counts = user_fields.map(fields => (fields(3), 1)).reduceByKey(_ + _).sortBy(_._2, false)
        println("用户数: %d, 性别数: %d, 职业数: %d, 邮编数: %d".format(num_users, num_genders, num_occupations, num_zipcodes))
        occupations_counts.foreach(println(_))
    }


}
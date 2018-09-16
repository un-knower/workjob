package sparkdemo.ml.base.basemodel

import org.apache.spark.ml.feature.{Word2Vec, Word2VecModel}
import org.apache.spark.sql.SparkSession

object Word_2_Vec {
    val sparkSession = SparkSession.builder().master("local[2]").appName("test").getOrCreate()
    val sc = sparkSession.sparkContext

    def main(args: Array[String]): Unit = {
        base1()
    }

    def base1(): Unit = {
        val documentDF = sparkSession.createDataFrame(Seq(
            "Hi I heard about Spark".split(" "),
            "I wish Java could use case classes".split(" "),
            "Logistic regression models are neat".split(" ")
        ).map(Tuple1.apply)).toDF("text")
        /*word2vecmodel使用文档中每个词语的平均数来将文档转换为向量，然后这个向量可以作为预测的特征，来计算文档相似度计算等等。*/
        val word2Vec = new Word2Vec().
                setInputCol("text").
                setOutputCol("result").
                setVectorSize(50).
                setMinCount(0).setNumPartitions(2)
        val model = word2Vec.fit(documentDF)
        val result = model.transform(documentDF)
        result.show(false)


    }


}

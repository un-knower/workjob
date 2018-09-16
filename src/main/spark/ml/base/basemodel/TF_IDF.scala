package sparkdemo.ml.base.basemodel

import org.antlr.v4.runtime.Vocabulary
import org.apache.spark.ml.feature.{HashingTF, IDF, Tokenizer}
import org.apache.spark.ml.linalg.SparseVector
import org.apache.spark.sql.SparkSession

import scala.collection.mutable


object TF_IDF {
    val sparkSession = SparkSession.builder().master("local[2]").appName("test").getOrCreate()
    val sc = sparkSession.sparkContext

    def main(args: Array[String]): Unit = {
        base1()
    }

    def base1(): Unit = {
        val sentenceData = sparkSession.createDataFrame(Seq(
            (0, "I heard about Spark and I love Spark"),
            (0, "I wish Java could use case classes"),
            (1, "Logistic regression models are neat")
        )).toDF("label", "sentence")
        // sentenceData 它作为hashingTF.transform的参数，要求每一行为一篇文档的内容,
        val tokenizer = new Tokenizer().setInputCol("sentence").setOutputCol("words")
        val wordsData = tokenizer.transform(sentenceData)
        wordsData.show(false)
        //  setNumFeatures(20)表最多20个词(这个很重要，否则可能就出现hash冲突，也就是哈希表的打下),hashing计算TF值,同时还把停用词(stop words)过滤掉了
        // HashingTF 是使用哈希表来存储分词，并计算分词频数（TF），生成HashMap表
        val hashingTF = new HashingTF().setInputCol("words").setOutputCol("rawFeatures").setNumFeatures(200)
        val featurizedData = hashingTF.transform(wordsData)
        // alternatively, CountVectorizer can also be used to get term frequency vectors
        featurizedData.show(false)
        featurizedData.foreach(println(_))

        val idf = new IDF().setInputCol("rawFeatures").setOutputCol("features")
        /* 训练模型 */
        val idfModel = idf.fit(featurizedData)
        /* 使用模型       问题是如何将输出结果和相应的单词对应起来 */
        val rescaledData = idfModel.transform(featurizedData)
        rescaledData.show(false)


    }
}

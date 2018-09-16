package ml.base.basemodel

import org.apache.spark.mllib.classification.SVMWithSGD
import org.apache.spark.mllib.evaluation.BinaryClassificationMetrics
import org.apache.spark.mllib.util.MLUtils
import org.apache.spark.sql.SparkSession

object SVMRegression {
    val sparkSession = SparkSession.builder().master("local[2]").appName("test").getOrCreate()
    val sc = sparkSession.sparkContext

    def main(args: Array[String]): Unit = {
        svmRegression()
    }

    def svmRegression(): Unit = {
        val data = MLUtils.loadLibSVMFile(sc, "data/mllib/sample_libsvm_data.txt")
        val splits = data.randomSplit(Array(0.6, 0.4), seed = 11L)
        val training = splits(0).cache()
        val test = splits(1)
        val numIterations = 100
        val model = SVMWithSGD.train(training, numIterations)
        model.clearThreshold()
        val scoreAndLabels = test.map { point =>
            val score = model.predict(point.features)
            (score, point.label)
        }
        scoreAndLabels.foreach(println(_))
        // Get evaluation metrics.
        val metrics = new BinaryClassificationMetrics(scoreAndLabels)
        println("metrics=" + metrics)
        val auROC = metrics.areaUnderROC()
        println("Area under ROC = " + auROC)
    }


}

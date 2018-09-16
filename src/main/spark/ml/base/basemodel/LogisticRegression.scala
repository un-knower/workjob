package ml.base.basemodel

import org.apache.spark.mllib.classification.{LogisticRegressionModel, LogisticRegressionWithLBFGS}
import org.apache.spark.mllib.evaluation.BinaryClassificationMetrics
import org.apache.spark.mllib.regression.LabeledPoint
import org.apache.spark.mllib.util.MLUtils
import org.apache.spark.sql.SparkSession

object LogisticRegression {
    val sparkSession = SparkSession.builder().master("local[2]").appName("test").getOrCreate()
    val sc = sparkSession.sparkContext
    def main(args: Array[String]): Unit = {
        logisticRegression()
    }
    def logisticRegression(): Unit ={
        val data = MLUtils.loadLibSVMFile(sc, "data/mllib/sample_libsvm_data.txt")
        val splits = data.randomSplit(Array(0.6, 0.4), seed = 11L)
        val training = splits(0).cache()
        val test = splits(1)
        val model = new LogisticRegressionWithLBFGS()
                .setNumClasses(10)
                .run(training)

        model.setThreshold(0.8)

        val predictionAndLabels = test.map { case LabeledPoint(label, features) =>
            val prediction = model.predict(features)
            (prediction, label)
        }
        predictionAndLabels.foreach(println(_))

        //二元矩阵
        val metrics = new BinaryClassificationMetrics(predictionAndLabels)
        //通过ROC对模型进行评估,值趋近于1 receiver operating characteristic (ROC), 接受者操作特征 曲线下面积
        val auROC: Double = metrics.areaUnderROC()
        println("Area under ROC = " + auROC)
        //通过PR对模型进行评估，值趋近于1 precision-recall (PR), 精确率
        val underPR: Double = metrics.areaUnderPR()
        println("Area under PR = " + underPR)
        //model.save(sc, "myModelPath")

    }

}

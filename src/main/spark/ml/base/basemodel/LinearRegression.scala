package ml.base.basemodel

import org.apache.spark.mllib.linalg.Vectors
import org.apache.spark.mllib.regression.{LabeledPoint, LinearRegressionModel, LinearRegressionWithSGD}
import org.apache.spark.sql.SparkSession

/**
  * 　LinearRegressionWithSGD
　*　 RidgeRegressionWithSGD
　*　 LassoWithSGD
  */
object LinearRegression {
    val sparkSession = SparkSession.builder().master("local[2]").appName("test").getOrCreate()
    val sc = sparkSession.sparkContext
    def main(args: Array[String]): Unit = {
        linearRegression()
    }

    def linearRegression(): Unit ={
        val data = sc.textFile("data/mllib/ridge-data/lpsa.data")
        val parsedData = data.map { line =>
            val parts = line.split(',')
            LabeledPoint(parts(0).toDouble, Vectors.dense(parts(1).split(' ').map(_.toDouble)))
        }.cache()
        // numIterations:迭代次数
        val numIterations = 100
        val model = LinearRegressionWithSGD.train(parsedData, numIterations)
        //获取特征权重，及干扰特征
        println("weights:%s, intercept:%s".format(model.weights,model.intercept))
        // 也可以直接使用RDD[Vector] 进行测试
        val valuesAndPreds = parsedData.map { point =>
            val prediction = model.predict(point.features)
            (point.label, prediction)
        }
        //计算 均方误差
        val MSE = valuesAndPreds.map{case(v, p) => math.pow((v - p), 2)}.mean()
        println("training Mean Squared Error = " + MSE)
        // 保存和加载模型的时候报错了
        model.save(sc, "modle/regression/myline")
        val sameModel = LinearRegressionModel.load(sc, "modle/regression/myline")
    }
}

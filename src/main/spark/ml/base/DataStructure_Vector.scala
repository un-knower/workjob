package sparkdemo.ml.base

import org.apache.spark.SparkContext
import org.apache.spark.mllib.linalg.{Matrices, Matrix, Vector, Vectors}
import org.apache.spark.mllib.recommendation.Rating
import org.apache.spark.mllib.regression.LabeledPoint
import org.apache.spark.mllib.util.MLUtils
import org.apache.spark.rdd.RDD
import org.apache.spark.sql.SparkSession

/*
* 对于稀疏矩阵脑海里要有它的存储方式
* 向量：
*   由double类型数值构成的有索引集合，索引从0开始
* */

object DataStructure_Vector {
    val sparksession = SparkSession.builder().master("local[2]").appName("matrix").getOrCreate()
    val sc: SparkContext = sparksession.sparkContext

    def main(args: Array[String]): Unit = {
        vecotr_action
    }

    /**
      * 本地向量（Local Vector）存储在单台机器上，索引采用0开始的整型表示，值采用Double类型的值
      * 分别是密度向量（Dense Vector）和稀疏向量（Spasre Vector），密度向量会存储所有的值包括零值，而稀疏向量存储的是索引位置及值
      * 需要注意的是，Scala默认会导入scala.datastructer.immutable.Vector包，所以必须手动导入org.apache.spark.mllib.linalg.Vector包才能使用MLlib的vector包。区别是是否要指定数据类型
      */
    def local_vector(): Unit = {
        val dv: Vector = Vectors.dense(1.0, 0.0, 3.0)
        // 创建稀疏向量，指定元素的个数、索引及非零值，数组方式
        val sv: Vector = Vectors.sparse(3, Array(0, 2), Array(1.0, 3.0))
        //  // 创建稀疏向量，指定元素的个数、索引及非零值，序列方式
        val sv2: Vector = Vectors.sparse(3, Seq((0, 1.0), (2, 3.0)))
        println(dv)
        println(sv)
        println(sv2)
    }

    /**
      *     带有类别特征的向量，也分为密度向量和稀疏向量
      *     spark 提供的分类、回归算法都只能作用于 LabeledPoint的Rdd 上
      *     由于lable 是一个double的值，所以它可以用来表示类别标签或数值标签
      */
    def lable_vecotor(): Unit = {
        val pos = LabeledPoint(1.0, Vectors.dense(1.0, 0.0, 3.0))
        val neg = LabeledPoint(0.0, Vectors.sparse(3, Array(0, 2), Array(1.0, 3.0)))
        println(pos)
        println(neg)
    }

    /**
      * Rating 用于推荐算法中，（用户id,物品id,评分），推荐算法中的数据必须是 RDD[Rating]
      */
    def  rating(): Unit ={
        val rating=Rating(100,10,3)
    }



    /*lable_vecotor 的存储 LIBSVM  label index1:value1 index2:value2 …进行特征标签及特征的存储与读取 ，数据使用空格隔开*/
    def lable_vector_file(): Unit = {
        val examples: RDD[LabeledPoint] = MLUtils.loadLibSVMFile(sc, "src/main/resources/learning/sample_libsvm_data.txt")
        println(examples)
    }

    /*对向量的一些操作  */
    def vecotr_action(): Unit = {
        val sv1 = Vectors.sparse(3, Array(0, 2), Array(1.0, 3.0))
        println(sv1.size, sv1, sv1(0), sv1(1), sv1(2))
        val pos = LabeledPoint(1.0, Vectors.dense(1.0, 0.0, 3.0))
        //获取标签
        println(pos.label)
        // 获取向量
        println(pos.features)
        println(pos.features.size)
        //将一个标签向量用list存储
        println(List(pos))

        val neg = LabeledPoint(2, sv1)
    }

}

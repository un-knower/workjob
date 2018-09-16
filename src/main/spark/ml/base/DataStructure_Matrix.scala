package sparkdemo.ml.base

import org.apache.spark.SparkContext
import org.apache.spark.mllib.linalg.distributed._
import org.apache.spark.mllib.linalg.{Matrices, Matrix, Vectors}
import org.apache.spark.mllib.regression.LabeledPoint
import org.apache.spark.mllib.util.MLUtils
import org.apache.spark.rdd.RDD
import org.apache.spark.sql.SparkSession

/**
  * 主要理解各个数据结构的特点和构造方法，所有的矩阵都是在RDD[A] 的基础上构造的，A 是各种vector
  * 稀疏矩阵的集中存储方式
  * COO  最容易理解的三元组
  * CSC  列压缩（行，列，列压缩，非零元素行号，值数组）
  */

/**
  * 分布式矩阵的行列索引是long类型的，数值是double型，它存储在一个或多个RDD中，选择合适的格式存储大型分布式矩阵非常重要。目前有三种数据格式
  * RowMatrix，最基本的类型，它没有有意义的行索引，每行是一个本地向量，如果假设列的数量不是非常大，在单个节点上可以被存储和处理,每一行是vector
  * IndexedRowMatrix 类似于前者，但是可以按照行来索引，每一行是 IndexedRow
  * CoordinateMatrix是一个coordinate(坐标) list (COO)格式的分布式矩阵，适用于特别稀疏的矩阵。
  */

object DataStructure_Matrix {
    val sparksession = SparkSession.builder().master("local[2]").appName("matrix").getOrCreate()
    val sc: SparkContext = sparksession.sparkContext

    def main(args: Array[String]): Unit = {


        val rdd = sc.textFile("src/main/resources/learning/test.data")
        distribute_CoordinateRowMatrix(rdd)

    }

    /**
      * 本地矩阵,通过特定的方法构建
      * 本地矩阵具有 整型 的行列下标和double型的值，MLlib支持密集向量
      * 将Array重组成一个rows行，cols列的矩阵；
      * Matrices.dense方法是矩阵重组的调用方法，该方法有三个参数：
      * 第一个参数是新矩阵的行数
      * 第二个参数是新矩阵的列数
      * 第三个参数为传入的数据值
      */
    def local_matrix(): Unit = {

        val dm: Matrix = Matrices.dense(3, 2, Array(1.0, 3.0, 5.0, 2.0, 4.0, 6.0))
        //（注，列式优先存储）
        val dms: Matrix = Matrices.sparse(10, 10, Array(0, 1, 1, 1, 2, 4, 4, 4, 5, 6, 6), Array(1, 3, 5, 6, 7, 11), Array(1.0, 3.0, 5.0, 2.0, 4.0, 6.0))
        println(dm.numCols, dm.numRows)
        println(dm)
        println(dms.numCols, dms.numRows)
        println(dms)

    }

    /**
      * 本地矩阵  也分为系数矩阵和密度矩阵
      */
    def local_matrix2(): Unit = {
        /*可以看出数据是按列进行分配的，就是按列填充*/
        val dm: Matrix = Matrices.dense(3, 2, Array(1.0, 3.0, 5.0, 2.0, 4.0, 6.0))
        /*矩阵的大小，列非零元素个数(第一个元素默认零，2 代表的前一列总共有2个非零元素，3代表前两列有三个) ，非零元素行号
        * 也可以这样理解：后面一个减去前面一个，构成一个数组，意思是该列有几个不为零的元素
        * */
        val sparseMatrix = Matrices.sparse(3, 3, Array(0, 2, 3, 6), Array(0, 2, 1, 0, 1, 2), Array(1.0, 2.0, 3.0, 4.0, 5.0, 6.0))
        println(dm)
        println(sparseMatrix)

    }

    def distribute_matrix_RowMatrix(rdd: RDD[String]): Unit = {
        /**
          * RowMatrix是一个行索引的分布式矩阵，但是它的行索引并没有实际意义。每行都是一个本地向量，因为每行都被一个本地向量代表，所以列的数量收到整数最大值的限制，
          * 但是实际操作中应该远小于上限值。
          * 下面的操作体现了dense()方法的两种参数
          */
        val rows: RDD[org.apache.spark.mllib.linalg.Vector] = rdd.map(_.split(",")).map(x => x match {
            case Array(_, _, _) => Vectors.dense(x(0).toDouble, x(1).toDouble, x(2).toDouble)
        })
        val rows2 = rdd.map(_.split(",").map(_.toDouble)).map(line => Vectors.dense(line))
        // 这里构分布式矩阵的时候参数的格式比较特殊
        val mat1: RowMatrix = new RowMatrix(rows)
        val mat2: RowMatrix = new RowMatrix(rows2)
        var m = mat1.numRows()
        var n = mat1.numCols()
        println(m, n)
        println(mat2)
    }

    def distribute_matrix_RowMatrix(): Unit = {
        val rdd1 = sc.parallelize(
            Array(
                Array(1.0, 2.0, 3.0, 4.0),
                Array(2.0, 3.0, 4.0, 5.0),
                Array(3.0, 4.0, 5.0, 6.0)
            )
        ).map(f => Vectors.dense(f))
        val rowMatirx = new RowMatrix(rdd1)
    }

    /*IndexedRowMatrix 可以转换成 RowMatrix */
    def distribute_IndexedRowMatrix(rdd: RDD[String]): Unit = {
        /**
          * IndexedRowMatrix和RowMatrix类似，但是行索引有其实际意义。每一行 被它的索引和一个本地向量表示。
          */
        var inedx: Long = 0;
        val indexrows: RDD[org.apache.spark.mllib.linalg.distributed.IndexedRow] = rdd.map(_.split(",").map(_.toDouble)).map(x => x match {
            case Array(_, _, _) => {
                val tmp = IndexedRow(inedx, Vectors.dense(x))
                inedx = inedx + 1
                tmp
            }
        })
        val mat2: IndexedRowMatrix = new IndexedRowMatrix(indexrows)
        val m = mat2.numRows()
        val n = mat2.numCols()
        println(m, n)
        // Drop its row indices.
        val rowMat: RowMatrix = mat2.toRowMatrix()
        mat2.rows.map(x => {
            x.index + "\t" + x.vector + "\t" + x.vector.size
        }).foreach(println)
        println("=============================================================")
        rowMat.rows.foreach(println)
    }

    /**
      * 坐标矩阵
      *
      * @param path 传入数据路径
      *             坐标矩阵一般用于数据比较多且数据较为分散的情形，即矩阵中含0或者某个具体值较多的情况下
      *             坐标矩阵类型格式如下:
      *             (x: Long, y: Long, value: Double)
      *             从格式上看，x和y分别代表标示坐标的坐标轴标号，value是具体内容
      *             x是行坐标，y是列坐标
      *             rdd下的最后一个map里，_1和_2这里是scala语句中元祖参数的序数专用标号;
      *             下划线前面有空格，告诉MLlib这里分别是传入的第二个和第三个值
      *             注意：直接打印CoordinateMarix实例对象也仅仅是内存地址
      *
      */
    def distribute_CoordinateRowMatrix(rdd: RDD[String]) = {
        val tmp: RDD[MatrixEntry] = rdd
                .map(_.split(",")
                        .map(_.toDouble))
                .map(line => (line(0).toLong, line(1).toLong, line(2))) //转换成坐标格式的元祖
                .map(line => new MatrixEntry(line _1, line _2, line _3)) //转化成坐标矩阵格式 和 IndexedRow 是一样的
        val crm = new CoordinateMatrix(tmp)
        crm.entries.map(x => x.i + "\t" + x.j + "\t" + x.value).foreach(println)
        println(crm.numRows())
    }

}

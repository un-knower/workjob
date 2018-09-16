package sparkdemo.complexjson

import org.apache.spark.sql.types._

/*
* StructField 实际上就像sql的字段，包含了该字段信息，是否为空，数据类型
* structType 可以有多个 StructField，实际上就是一个对象也就是一个记录，同时也可以用名字（name）来提取,就想当于Map可以用key来提取value
* */
object StructTypeLearn {
    def main(args: Array[String]): Unit = {
        test1()
    }

    def test1(): Unit = {
        val struct =
            StructType(
                StructField("a", IntegerType, true) ::
                        StructField("b", LongType, false) ::
                        StructField("c", BooleanType, false) :: Nil)
        struct.printTreeString()
        //提取一个structfiled  不存在则
        println(struct("a"))
    }


}

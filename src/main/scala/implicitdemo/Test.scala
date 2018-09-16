package scala.implicitdemo

import org.apache.spark.sql.types.StructType
import util.ImplicitUtils.defineSchema

object Test {

    def main(args: Array[String]): Unit = {
        test(defineSchema("a,b,c"))
    }

    def test(s: StructType): Unit = {
        println(s)
    }

}

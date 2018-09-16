package other.annotation

import scala.beans.BeanProperty

/**
  * 基础示例
  */
object AnnotationDemo1 {
    // 生成字段的get set 方法
    @BeanProperty
    val s="kingcall"
    def main(args: Array[String]): Unit = {
        val x=Map("a"->1)
        val x1=10
        test1(x)
        dep()
    }
    @deprecated("该方法即将在下一个版本中被取消")
    def dep(): Unit ={
        println("显示输出的结果")
        println(getS())
    }

    /**
      * 注意空格
      * @param x
      */
    def test1(x:Any)=(x: @unchecked) match {
        case a:String=>println("字符串数据类型")
        case a:Map[String,String]=>println("Map 数据类型")
    }
}

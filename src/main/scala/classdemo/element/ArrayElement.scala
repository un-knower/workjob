package scala.classDemo.element

// 使用字段的方式重写
class ArrayElement(content:Array[String]) extends Element{
  //override def contents: Array[String] = conts
  // 使用字段实现了方法的重写
  val contents:Array[String] = content
}
// 使用方法的方式重 写
class ArrayElement2(content:Array[String]) extends Element{
  override def contents: Array[String] = content
}

// 参数化字段的实现
class ArrayElement3(val contents:Array[String]) extends Element{
}
class ArrayElement4(var contents:Array[String]) extends Element{
}

object test{
  def main(args: Array[String]): Unit = {
    val element = new ArrayElement(Array("abcdefg","ABCDEFG"))
    println(element.contents)
    println(element.height)
    println(element.wight)
    println()
  }
}
package functiondemo

/**
  * 可以看出函数的定义方式十分多样
  */
object PartialFunction {
  def main(args: Array[String]): Unit = {
    println(p2(0))
    println(p3(0))
    println(p1(0))
    println(p4(4))
    // 检测偏函数对某个参数是否支持
    println(p5.isDefinedAt(List(1,2,3)))
    println(p5(List()))
  }
  // 关于这个类型，第一个是输入，第二个是输出
  def p1:PartialFunction[Int,Any] = {
    case x if x > 1 => 1
    case _ =>"没匹配上"
  }

  def p2 = (x:Int) => x match {
    case x if x > 1 => 1
    case _ =>"没匹配上"
  }
  def p3(x:Int)={
    x match {
      case x if x > 1 => 1
      case _ =>"没匹配上"
    }
  }
  val p4=(x:Int)=>x match {
    case x:Int if x > 1 => 1
    case _ =>"没匹配上"
  }
  val p5:PartialFunction[List[Int],Int]={
    case x::y::_=>y
  }



}

package classdemo


object LeazyDemo {
  object s1{
    val x={println("初始化被执行 s1");"kingcall"}
  }
  object s2{
    lazy val x={println("初始化被执行 s2");"kingcall"}
  }

  def main(args: Array[String]): Unit = {
    test2()
  }
  def test2(): Unit ={
    s2
    println("==================================")
    s2.x
  }
  def test1(): Unit ={
    s1
  }

}

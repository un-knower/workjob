package classdemo

/**
  * 特质的初始化过程和
  */
trait RationalTrait {
   val num:Int
  val den:Int
  require(den!=0&&num!=0)
}
trait RationalTrait2 {
   val num:Int
   val den:Int
  require(den!=0&&num!=0)
}

object Test {
  def main(args: Array[String]): Unit = {
    rightest2()
  }
  def lazymethod(): Unit ={

  }
  // 预初始化的方法
  def rightest(): Unit = {
    val x = new {
      override val num: Int = 10
      override val den: Int = 20
    } with RationalTrait
    println(x.den)
  }
  // 惰性变量的使用
  def rightest2(): Unit ={
    val x=new RationalTrait {
      override lazy val num: Int = 10
      override lazy val den: Int = 20
    }
    println(x.den)
  }

  def erroetest(): Unit ={
    val x=new RationalTrait {
      override val num: Int = 10
      override val den: Int = 20
    }
    println(x.den)
  }
}


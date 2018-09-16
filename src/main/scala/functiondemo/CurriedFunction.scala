package functiondemo

/**
  * @program: workjob
  * @description: ${description}
  * @author: 刘文强 kingcall
  * @create: 2018-08-02 23:42
  **/
object CurriedFunction {
  def main(args: Array[String]): Unit = {
    println(sum1(1)(2))
    println(sum2(1)(2))
    println(sum3(1)(2))
    println(sum1(1)(_:Int))
    println(sum1(1)_)
    println(sum1(1) _)

  }

  def sum1(x:Int)(y:Int)=x+y

  def sum2(x:Int)={
     // 注意这里是函数字面量
    (y:Int)=>x+y
  }

  def sum3(x:Int)={
    def sum(y:Int)={
      x+y
    }
    // 打包：部分应用函数
    sum _
  }

}

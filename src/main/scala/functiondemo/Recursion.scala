package functiondemo

object Recursion {
  def main(args: Array[String]): Unit = {
    println(test1(0,10))
    println(test2(0,10))
  }

  /**
    * 将while 循环编程了递归的方式
    * @param init
    * @param x
    * @return
    */

  def test2(init:Int,x:Int):Int={
    if(init<x){
      val result=init+1
      test2(result,x)
    }
    else
      init
  }

  def test1(init:Int,x:Int): Int ={
    var result=0
    while(result<x){
      result=result+1
    }
    result
  }

}

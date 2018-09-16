package functiondemo

object RecursionTail {
  def main(args: Array[String]): Unit = {
    println(boom(10))
  }

  /**
    * 为递归优化
    * @param x
    * @return
    */
  def boom2(x:Int): Int = {
    if (x == 0) throw new Exception("some thing is error")
    else boom2(x - 1)
  }
  def boom(x:Int): Int ={
    if(x==0) throw new Exception("some thing is error")
    else boom(x-1)+1
  }

}

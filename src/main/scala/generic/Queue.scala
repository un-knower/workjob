package generic
class Queue[T]  (private val leading:List[T],
                        private val tailing:List[T]
){
  def head=leading
  def tail=tailing
  def enqueue(x:T)=x::leading
}


class cell[T](init:T){
  private[this] var current=init
  def get=current
  def set(x:T)={current=x}
}

object tets{
  def main(args: Array[String]): Unit = {
    test3
  }
  def test3(): Unit ={
    val x=new Queue[String](List("1"),List("2"))
    println(x.head)
    println(x.enqueue("3"))
  }
  //这个操作在java中很容易完成，但是在scala中却需要处理
  def test2(): Unit ={
    val a1=Array("abc")
    val a2:Array[_ <: Any]=a1

  }
  def test1(): Unit ={
    val c1=new cell[String]("kingcall")
    println(c1.get)
    c1.set("sss")
    println(c1.get)
  }
}
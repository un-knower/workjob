package implicitdemo

/**
  * 类型参数化可以让我们编写泛型的类和特质
  */
object Typeparamete {


}

class SlowQueue[T](elems:List[T]){
  def head=elems.head
  def tail=new SlowQueue[T](elems.tail)
  def enqueue(x:T)=new SlowQueue[T](elems:::List(x))
}
class HighQueue[T](elems:List[T]){
  val list=elems.reverse
  def head=elems.last
  def tail=elems.tail
  def enqueue(x:T)=new SlowQueue[T](elems:::List(x))
}

package generic

/**
  * @program: Default (Template) Project
  * @description:
  * @author: 刘文强 kingcall
  * @create: 2018-08-08 10:56
  **/
class HighQueue[T](elems:List[T]){
  val list=elems.reverse
  def head=elems.last
  def tail=elems.tail
  def enqueue(x:T)=new SlowQueue[T](elems:::List(x))
}

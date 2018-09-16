package classdemo

/**
  * 1. 辅助构造器的名称为this
  * 2. 每个辅助构造器都必须以一个对先前已定义的其他辅助构造器或主构造器的调用开始
  **/
class Queue[T] private (
                         private val leading:List[T],
                         private val tail:List[T]
                       ){
  override def toString: String = leading.mkString("\t")+"\t"+tail.mkString("\t")
}
// 伴生对象可以访问class 的私有构造方法
object Queue{
  def apply[T](x:List[T],y:List[T]): Queue[T] = new Queue[T](x.toList,y.toList)
}

object PrivateDemo {
    def main(args: Array[String]): Unit = {
        val x=Queue[String](List("a"),List("b"))
        println(x)
    }

}

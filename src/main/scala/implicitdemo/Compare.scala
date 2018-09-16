package implicitdemo

import scala.util.Sorting

/**
  * Ordered混入（mix）Java的Comparable接口，而Ordering则混入Comparator接口。众所周知，在Java中
  *  实现Comparable接口的类，其对象具有了可比较性；
  *  实现comparator接口的类，则提供一个外部比较器，用于比较两个对象。
  */

case class Person(name: String, age: Int) {
  override def toString = {
    "name: " + name + ", age: " + age
  }
}

object Compare {
  implicit object PersonOrdering extends Ordering[Person] {
    override def compare(p1: Person, p2: Person): Int = {
      p1.name == p2.name match {
        case false => -p1.name.compareTo(p2.name)
        case _ => p1.age - p2.age
      }
    }
  }

  def main(args: Array[String]): Unit = {
    ordered_op()
  }
  def test_Ordering(): Unit ={
    val p1 = new Person("rain", 13)
    val p2 = new Person("rain", 14)
  }

  def test_other(): Unit ={
    val pairs = Array(("a", 5, 2), ("c", 3, 1), ("b", 1, 3))
    Sorting.quickSort[Tuple3[String,Int,Int]](pairs)
    println(pairs.mkString("\t"))
    /*下面这个操作表示没看懂 Ordering.by[(String, Int, Int), Int](_._2) 的定义很有意思*/
    Sorting.quickSort(pairs)(Ordering.by[(String, Int, Int), Int](_._2))
    println(pairs.mkString("\t"))
    println(Ordering.by[(String, Int, Int), Int](_._2))
  }

  /**
    * 对一个集合进行自然排序，通过传递隐式的Ordering
    * 还有对对象的排序（元祖也可以）
    */
  def ordered_op(): Unit ={
    val xs=Seq(1,5,3,4,6,2)
    println("==============sorted排序=================")
    println(xs.sorted) //升序
    println(xs.sorted.reverse) //降序
    println("==============sortBy排序=================")
    println( xs.sortBy(d=>d) ) //升序
    println( xs.sortBy(d=>d).reverse ) //降序
    println("==============sortWith排序=================")
    println( xs.sortWith(_<_) )//升序
    println( xs.sortWith(_>_) )//降序
  }

}

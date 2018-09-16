package classdemo.traitlearn

import scala.collection.mutable.ArrayBuffer

/**
  * @program: workjob
  * @description: ${description}
  * @author: 刘文强 kingcall
  * @create: 2018-08-04 07:49
  **/
class BasicIntQueue extends IntQueue  {
  private val buf = new ArrayBuffer[Int]()
  override def get(): Int = buf.remove(0)
  override def put(x: Int) = buf+=x

}
object Test extends App {
  private val queue = new BasicIntQueue()
  queue.put(10)
  queue.put(30)
  println(queue.get())
}

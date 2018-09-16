package classdemo.traitlearn

/**
  * @program: workjob
  * @description: ${description}
  * @author: 刘文强 kingcall
  * @create: 2018-08-04 08:28
  **/
class MyIntQueue extends BasicIntQueue  with Incrementing with Filtering  with Doubling  {

}
object TestMe extends App {
  private val queue = new MyIntQueue()
  queue.put(-1)
  queue.put(10)
  println(queue.get())
  println(queue.get())
}

package classdemo.abstractdemo

/**
  * @program: workjob
  * @description: ${description}
  * @author: 刘文强 kingcall
  * @create: 2018-08-09 21:50
  **/
class Concrete extends Abstract {
   type T = String
   def transform(x: T):T = {
    x.toLowerCase
  }

   var current: T = "haha"
  override val initial: T = "呵呵"
}
object tets{
  def main(args: Array[String]): Unit = {
    val s=new Concrete;
    println(s.initial)
  }
}

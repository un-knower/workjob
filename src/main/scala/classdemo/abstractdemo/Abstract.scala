package classdemo.abstractdemo

/**
  * @program: workjob
  * @description: ${description}
  * @author: 刘文强 kingcall
  * @create: 2018-08-09 21:48
  **/
trait Abstract {
  type T
  def transform(x:T):T
  val initial:T
  var current:T

}

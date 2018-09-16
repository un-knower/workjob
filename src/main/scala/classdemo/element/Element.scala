package scala.classDemo.element

/**
  * @program: workjob
  * @description: ${description}
  * @author: 刘文强 kingcall
  * @create: 2018-08-03 21:47
  **/
abstract class Element {
  def contents:Array[String]
  def height:Int=contents.length
  def wight:Int=if(height==0)0 else contents(0).length
  def hight():Int=height

}

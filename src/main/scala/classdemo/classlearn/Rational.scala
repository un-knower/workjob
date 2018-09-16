package classdemo.classlearn

/**
  * @program: workjob
  * @description: ${description}
  * @author: 刘文强 kingcall
  * @create: 2018-07-31 22:07
  **/
class Rational(n:Int,d:Int) {
  require(d!=0)
  private val g=gcd(n,d)
  val num=n/g
  val dum=d/g
  def this(n:Int)=this(n,1)
  override def toString: String = num+"/"+dum
  def add(that:Rational): Rational ={
   new Rational( num*that.dum+that.num*dum,dum*that.dum)
  }
  def + (that:Rational)=add(that)
  def + (that:Int)=add(new Rational(that))
  def *(that:Rational)=new Rational(num*that.num,dum*that.dum)
  def *(that:Int)=new Rational(num*that,dum)
  def -(that:Rational)=new Rational(num*that.dum-that.num*dum,dum*that.dum)
  def -(that:Int)=new Rational(num-that*dum,dum)
  def /(that:Rational)=new Rational(num*that.dum,dum*that.num)
  def /(that:Int)=new Rational(num,dum*that)
  def lessThan(that:Rational)={
    num*that.dum<dum*that.num
  }
  def maxThan(that:Rational)={
    num*that.dum>dum*that.num
  }
  def max(that:Rational): Rational ={
    if(lessThan(that)) that else this
  }
   def equals(that: Rational): Boolean = {
    if(!lessThan(that) && !maxThan(that)) true else false
  }
  def >=(that:Rational)=maxThan(that) || equals(that)
  def <=(that:Rational)=lessThan(that) || equals(that)
  def <(that:Rational)=lessThan(that)
  def >(that:Rational)=maxThan(that)
  def ==(that:Rational)=equals(that)
  private def gcd(a:Int,b:Int):Int={
    if(b==0) a else gcd(b,a%b)
  }
  // 那我刚才在前面就直接用隐式转换不就行了啊，但是没有在作用域内，所以在下面的object中重新写了一个
  implicit def intToRational(int: Int)=new Rational(int)
}

object test extends App{
  implicit def intToRational(int: Int)=new Rational(int)
  println(new Rational(3,6) +3)
  println(3+ new Rational(3,6) )
}

package classdemo.classlearn

/**
  * @program: workjob
  * @description: ${description}
  * @author: 刘文强 kingcall
  * @create: 2018-07-31 22:07
  **/
class Rational2(n:Int,d:Int) extends Ordered[Rational2]{
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

  private def gcd(a:Int,b:Int):Int={
    if(b==0) a else gcd(b,a%b)
  }

  override def compare(that: Rational2): Int =this.num*that.dum-this.dum*that.num
}
object test2 extends App {
  println(new Rational2(3,6))
  println(new Rational2(1,2))
  println(new Rational2(1,2) ==  new Rational2(3,6))
  println(new Rational2(1,2) eq   new Rational2(3,6))
  println(new Rational2(1,2) equals   new Rational2(3,6))
  println(new Rational2(1,2)>new Rational2(3,6))
  println(new Rational2(1,2)<new Rational2(3,6))
}



package generic

/**
  * 如果T’是T的一个子类，那么Container[T’]应该被看做是Container[T]的子类吗？对于这个问题有三种可能的结果
  * 若类U >: T：
      *定义class A[T],此时A[U]与A[T]是没有任何关系的两个类;
      *若定义为class A[+T]，则有A[U] >: A[T](协变);
      *若定义为class A[-T]，则有A[U] <: A[T](逆变)。
    *一般地，「不可变的」(Immutable)类型意味着「型变」(Variant)，而「可变的」(Mutable)意味着「不变」(Nonvariant)。
        *如果它是一个生产者，其类型参数应该是协变的，即C[+T]；
        *如果它是一个消费者，其类型参数应该是逆变的，即C[-T]
 **
 逆变(Contravariant)的类型参数T只可能作为函数的参数；
        *协变(Covariant)的类型参数R只可能作为函数的返回值；
        *不变的(Nonvariable)类型参数S则没有限制，即可以作为函数的参数，也可以作为返回值
 **
         协变是可以用自己替换需要自己父亲的位置而是允许的，也就是当参数需要父类型参数时你可以传入子类型
        *逆变就是可以用父亲替换儿子的位置而是允许的，也就是当参数需要子类型的时候可以传入父类型
    *scala语言相比java语言提供了更多的灵活性，当不指定协变与逆变时，它和java是一样的
 *
  * @tparam T
  */
class Covariant[+T]
class Contravariant[-A]
// 下面就是一个错误的演示，A 是协变的，所以不能作为函数的参数

object Covariant_Contravariant extends App {
  val cv: Contravariant[String] = new Contravariant[AnyRef]
  val cv2: Covariant[AnyRef] = new Covariant[String]
  println(cv)
  println(cv2)
}

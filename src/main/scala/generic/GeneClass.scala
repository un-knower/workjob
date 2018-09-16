package generic

/**
  * 当构建一个类或者函数时，如果我们不知道（或者说不确定）传入的参数的具体数据类型，这时候可以泛型，
  * 在Java里，泛型类型都是invariant，比如 List<String> 并不是 List<Object> 的子类型。
  * 不过Java支持使用点变型(use-site variance)，所谓“使用点“，也就是在声明变量时:  List<? extends Object> list = new ArrayList<String>();
  * cala为了兼容java泛型通配符的形式，引入存在类型 val a : List[_ <: Any] = List[String]("A")
  */
object test0 {

    def main(args: Array[String]): Unit = {
        test3()
    }

    def test3(): Unit ={
        val pair=new Pair2_1(3,2);
        println(pair.smaller)

    }
    def test2(): Unit ={
        // pair就不能这样用，Pair2之所以可能是因为存在这样的隐式函数,可以将int 转化为RichInt
        // 之所以可以不用传类型参数进去，是因为可以自动推断
        val pair=new Pair2(2,3)
        println(pair.smaller)

    }

    def test1(): Unit ={
        val str = "123"
        val intv =123
        val strTest = new GeneClass[String](str)
        val intTest = new GeneClass[Int](intv)
        strTest.check;
        intTest.check;
    }
    def test[T](x:T): Unit ={

    }


}
//参数的界定是用来限制传入的参数的类型，否则该参数可能没有 compareTo 方法
class Pair[T<:Comparable[T]](val first:T,val second:T){
    def smaller:T = if(first.compareTo(second) < 0) first else second
}

// < % 这个界定符可以隐式地转换类型,但是你得提供这样的函数(如果存在也可以)
class Pair2[T<%Comparable[T]](val first:T,val second:T){
    def smaller = if(first.compareTo(second) < 0) first else second
}
// 使用类型约束替换试图界定，下面的意思也是存在这样的隐式转换（并且上面的编译后会被编译成此种形式）
class Pair2_1[T](val first:T,val second:T)(implicit ev:T=>Comparable[T]){
    def smaller = if(first.compareTo(second) < 0) first else second
}





class Pair3[T<:Comparable[T]](val first:T,val second:T){
    def smaller:T = if(first.compareTo(second) < 0) first else second
    //def replace[R](newfirst:R):Pair3[R]=new Pair3(newfirst,second)  返回的 pair3[Any]
//    def replace[R>:T](newfirst:R):Pair3[R]=new Pair3(newfirst,second)
}



class GeneClass[T](v:T) {
    def check = {
        if(v.isInstanceOf[String]){
            println("the param is String");
        }
        if(v.isInstanceOf[Int]){
            println("the param is Int");
        }
    }

}

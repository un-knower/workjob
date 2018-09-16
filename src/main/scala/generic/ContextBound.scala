package generic

import implicitdemo.MaxList.maxListOrdering

object ContextBound {
    def maxList[T](elements:List[T])(implicit ordering: Ordering[T]):T={
        elements match {
            case List()=>throw new IllegalArgumentException("empty list")
            case List(x)=>x
            case x::rest=>{
                val maxRest=maxList(rest)
                if(ordering.gt(x,maxRest)) x else maxRest
            }
        }
    }
    def maxList2[T](elements:List[T])(implicit ordering: Ordering[T]):T={
        elements match {
            case List()=>throw new IllegalArgumentException("empty list")
            case List(x)=>x
            case x::rest=>{
                val maxRest=maxList(rest)
                // implicitly[FOO] 返回一个 隐式的FOO 的对象，就是参数 ordering
                if(implicitly[Ordering[T]].gt(x,maxRest)) x else maxRest
            }
        }
    }

    /**
      * 上下文界定
      *     1.  [T:Ordering] 有一个以隐式的参数 [Ordering[T]]  ; 引进了参数类型 T
      *     2.  implicitly[Ordering[T]] 在当前上下文中寻找  Ordering[T] 类型的隐式对象，并返回
      * @param elements
      * @tparam T
      * @return
      */
    def maxList3[T:Ordering](elements:List[T]):T={
        elements match {
            case List()=>throw new IllegalArgumentException("empty list")
            case List(x)=>x
            case x::rest=>{
                val maxRest=maxList(rest)
                if(implicitly[Ordering[T]].gt(x,maxRest)) x else maxRest
            }
        }
    }


    def main(args: Array[String]): Unit = {
        test2()
    }
    def test2(): Unit ={
        case class Student(age:Int,name:String)
        implicit val ordering=new Ordering[Student] {
            override def compare(x: Student, y: Student): Int = x.age-y.age
        }
        val max:Student=maxListOrdering(List(Student(11,"kingcall"),Student(12,"call")))
        println(max)
    }

    def test1(): Unit ={
        case class Student(age:Int,name:String)
        implicit val ordering=new Ordering[Student] {
            override def compare(x: Student, y: Student): Int = x.age-y.age
        }
        val max:Student=maxListOrdering(List(Student(11,"kingcall"),Student(12,"call")))
        println(max)
    }

}

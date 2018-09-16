package implicitdemo

import scala.annotation.tailrec

object MaxList {
    // 需要的是一个对象
    def maxListOrdering[T](elements:List[T])(implicit ordering: Ordering[T]):T={
        elements match {
            case List()=>throw new IllegalArgumentException("empty list")
            case List(x)=>x
            case x::rest=>{
                // 在这里我们可以省去(ordering)
                val maxRest=maxListOrdering(rest)
                if(ordering.gt(x,maxRest)) x else maxRest
            }
        }
    }
    //下面这种实现更自由，需要的是一个方法，所以名字一定要起得形象，不然完全不知道这是干什么的
    def maxListOrdering2[T](elements:List[T])(implicit ordering:(T,T)=>Boolean):T={
        elements match {
            case List()=>throw new IllegalArgumentException("empty list")
            case List(x)=>x
            case x::rest=>{
                val maxRest=maxListOrdering2(rest)(ordering)
                if(ordering(x,maxRest)) x else maxRest
            }
        }
    }
    def main(args: Array[String]): Unit = {
        test2
    }
    def test3(): Unit ={
        case class Student(age:Int,name:String)
         val ordering=new Ordering[Student] {
            override def compare(x: Student, y: Student): Int = x.age-y.age
        }
        implicit val method=ordering.gt _
        val max:Student=maxListOrdering2(List(Student(11,"kingcall"),Student(12,"call")))
        println(max)

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
        val max= maxListOrdering(List(1,2,3,4,5,6))
        println(max)
    }

}

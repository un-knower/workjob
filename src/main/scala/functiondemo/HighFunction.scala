package scala.functiondemo


import scala.math._

object HighFunction {

    def main(args: Array[String]): Unit = {
        partial_functon()
    }

    /*如何把一个函数复制给一个变量*/
    def FunAsVal: Unit = {
        val num = 3.14
        /*下划线的意思是 我确实指的是这个函数  而不是忘记给它传参数了*/
        val fun = ceil _
        println(fun(num))
        /*在一个预期的函数上下文中使用   _ 是可以没有的*/
        val f: (Double) => Double = ceil
        println(f(num))
        Array(3.14, 4.52, 6.18).map(f).map(print _)
        println()
        Array(3.14, 4.52, 6.18).map((x: Double) => ceil(x)).map(print _)
    }

    /*返回值是函数的高阶函数*/
    def FunParameterAndResult(): Unit = {
        /*定义以一个返回值是函数的高阶函数*/
        def mulby(factor: Double) = (x: Double) => factor * x

        def mulby2(factor: Double) = {
            val p = (x: Double) => factor * x
            p
        }

        val DoubleFun5 = mulby(5)
        println(DoubleFun5(3))
    }

    /*柯里化------>将两个参数的函数变为关于第二个参数的函数*/
    def CurryingFunction() {
        /*演示一*/
        val mul = (x: Int, y: Int) => x * y
        val mulOne = (x: Int) => ((y: Int) => x * y)
        println(mul(3, 4))
        println(mulOne(3)(4))

        /*演示二*/
        def mulOneT(x: Int)(y: Int): Int = {
            x * y
        }

        println(mulOneT(3)(4))
        val mulOneTT = (x: Int) => (y: Int) => x * y;
        println(mulOneTT(3)(4))
    }

    def abountPara(): Unit = {
        val x = Array(1, 2, 3, 4)
        val y = x.map(addOne)
        println(y.mkString("\t"))
    }

    def addOne(x: Int): Int = {
        x + 1;
    }

    /**
      * fold 和 reduce的区别是 fold 需要提供一个初始值
      */
    def fold_op(): Unit = {
        val numbers = List(5, 4, 8, 6, 2)
        val result = numbers.fold(0)(_ + _)
        println(result)
    }

    /**
      * Scala中的Partial Function就是一个“残缺”的函数，就像一个严重偏科的学生，只对某些科目感兴趣，而对没有兴趣的内容弃若蔽履
      * 对给定的输入参数类型，函数可接受该类型的任何值。换句话说，一个(Int) => String 的函数可以接收任意Int值，并返回一个字符串。
      * 对给定的输入参数类型，偏函数只能接受该类型的某些特定的值。一个定义为(Int) => String 的偏函数可能不能接受所有Int值为输入。
      * 由于它仅仅处理输入参数的部分分支，因而它通过isDefineAt()来判断输入值是否应该由当前偏函数进行处理
      */
    def partial_functon(): Unit = {
        val pf: PartialFunction[Int, String] = {
            case 1 => "One"
            case 2 => "Two"
            case 3 => "Three"
            case _ => "Other"
        }

//        val pf2:PartialFunction[Int, String] = {
//            def isDefinedAt(x:Int) ={if(x%2==0) true else false}
//            def orElse()
//
//
//        }
        println(pf.apply(5))

    }


}

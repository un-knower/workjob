package scala.functiondemo

/**
  * 函数的定义  1.常用的定义方法   2. 参数的特殊格式
  */
object Functiondemo {
    /**
      * 匿名函数
      *   1. 函数字面值 (s: String) => s.toLowerCase
      *   2. new Function1[String, String] {def apply(s: String): String = s.toLowerCase}
      * 其中，Function1[String, String]可以简写为String => String  所以又可以写成 new (String => String) {def apply(s: String): String = s.toLowerCase}
      * 总结：
      * 函数实际上是FunctionN[A1, A2, ..., An, R]类型的一个实例而已。例如，(s: String) => s.toLowerCase是Function1[String, String]类型的一个实例。
      * 3.
      *
      */

    def test1(x: Int) = {
        x + 3
    }

    /**
      * 显示的指明函数的返回值可以起到类型转换的作用
      *
      * @param x
      * @return
      */
    def test2(x: Int): Double = {
        x + 3
    }

    /**
      * 在函数式编程中，函数做为一等公民，函数值可以被自由地传递和存储。例如，它可以赋予一个变量lower
      * => 的意思是函数
      **/
    def test3(): Unit = {
        val lower: String => String = _.toLowerCase
        println(lower("ABC"))
        val lower2 = (s: String) => s.toLowerCase
        println(lower2("ABC"))
    }

    /**
      * 有名函数  相对于「匿名函数」，如果将「函数值」赋予def，或者val，此时函数常称为「有名函数」(Named Function)。
      */
    def test4(): Unit = {
        val lower1: String => String = _.toLowerCase

        def lower2: String => String = _.toLowerCase

        println(lower1.equals(lower2))
    }

    /**
      * 函数的调用：在Scala里，「函数调用」实际上等价于在FunctionN实例上调用apply方法
      */
    def test5(): Unit = {
        println("====================== test5 ======================")
        val lower: String => String = _.toLowerCase
        println(lower("ABC"))
        println(lower.apply("ABC"))
    }

    /**
      * 函数定义的一个通用形式
      * (a1: A1, a2: A2, ..., an: An) => E:R =         或者    FunctionN[A1, A2, ..., An, R]
      *
      */

    /**
      * 函数和方法：
      * 两者都是使用了def定义的「方法」(Method)。但是，后者返回了一个「函数类型」，并具有函数调用的语义,函数有一个函数字面值
      * 所以目前在项目中定义的更多的是方法而不是函数
      */
    def test6(): Unit = {
        println("====================== test6 ======================")

        def lower1(s: String): String = s.toLowerCase

        def lower2: String => String = _.toLowerCase

        println(lower1("ABC"))
        println(lower2("ABC"))
        /*
        方法是没有值的，因此它不能赋予给变量
         */
        val lower3: String => String = _.toLowerCase()
        println(lower3("ABC"))
        // lower1 是不能这样赋值的
        val lower4 = lower2
        println(lower4("ABC"))
    }

    /**
      * 对方法lower实施η扩展，将其转换为部分应用函数
      * lower _将生成一个匿名的函数对象
      * 当上下文需要一个「函数类型」时，此时编译器可以自动完成「方法」的η扩展。
      */
    def test7(): Unit = {
        println("====================== test7 ======================")

        def lower1(s: String): String = s.toLowerCase

        val s = lower1 _
        val s2 = lower1(_)
        println(s("ABC"))
        println(s2("ABC"))
        println(List("A", "B", "C").map(lower1 _))
        println(List("A", "B", "C").map(lower1))
        println(List("A", "B", "C").map(lower1(_)))
    }

    /**
      * 两阶段调用
      * 此处的lower是一个使用def定义的方法。但是特殊地，它返回了函数类型
      */
    def test8(): Unit = {
        println("====================== test8 ======================")

        def lower: String => String = _.toLowerCase

        println(lower("ABC"))

    }

    /**
      * def与val ，定义了类似的有名函数，但两者之间存在微妙的差异。
      * 前者使用val的变量直接持有函数值，多次使用lower将返回同一个函数值
      */
    def test9(): Unit = {
        println("====================== test9 ======================")
        val lower1: String => String = _.toLowerCase

        def lower2: String => String = _.toLowerCase

        println(lower1 eq lower1)
        println(lower2 eq lower2)
    }

    /**
      * 函数的各种简化写法
      */
    def test10(): Unit = {
        println("====================== test10 =====================")
        var f = (x: Int) => x + 1
        println(f(3))
        println(f(4))

    }


    def main(args: Array[String]): Unit = {
        println(test1(3).isInstanceOf[Int])
        println(test2(3).isInstanceOf[Int])
        test3()
        test4()
        test5()
        test6()
        test7()
        test8()
        test9()
        test10()
    }


}

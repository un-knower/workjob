package scala.other


object NullDemo {
    /**
      * Null是一个Trait，你不能创建她它的实例,但是Scala在语言层面上存在一个Null的实例，那就是null。Java中的null意味着引用并没有指向任何对象。
      * 但存在一个悖论，一切都是对象，那没有对象是不是也是对象呢？Scala定义了一个类似于对象语义的Null，和一个值语义的null。这样面向对象在空引用的情况下完备了
      * 如果你写了一个带有Null作为参数的对象，那么你传入的参数只能是null，或者指向Null的引用
      */
    def try_null(thing: Null): Unit = {
        println("That worked!")
    }

    /**
      * Nil是一个继承List[Nothing]的对象，我们随后讨论Nothing。它就是一个空的列表
      * Nil就是一个可以封装任何东西的空容器。它的长度为0。它并不是一无所有，它是一个容器，一个列表，只是没有存放内容而已
      * 就像一个list的tail 永远是一个 Nil
      */

    def try_Nil(): Unit = {
        val digits = List(1, 2, 3)
        println(digits.tail)
        val digits2 = List()
        //.UnsupportedOperationException: tail of empty list  对于孔列表不支持tail操作
        println(digits2)
        println(Nil)
        println(Nil == digits2)

    }


    def main(args: Array[String]): Unit = {
        try_null(null)
        val someRef: String = null

        /**
          * 想清楚这个为什么会报错  虽然上面的变量也是指向空的，在编译阶段认为它是string  运行阶段它可能是空
          */
        //try_null(someRef)
        val nullRef: Null = null
        try_null(nullRef)
        try_Nil()
    }
}

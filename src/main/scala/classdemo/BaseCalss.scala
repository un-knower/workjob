package scala.classDemo

import scala.beans.BeanProperty

/**
  *  1. Scala源文件可以包含多个类，所有这些类都具有可见性，这与C++一样，与Java有很大的不同。
  *  2. 当方法或者构造该方法没有参数的时候可以不写括号(方法的定义也一样),但是反过来就不行，没有括号的时候带上括号就不行
  *     1. 获取对象属性的时候一般不写括号
  *     2. 修改对象属性的时候一般习惯待括号
  *  3. 字段
  *     1. age的getter和setter方法则是公有的
  *     2. 但是声明为private后，age的getter和setter方法则是私有的，因为对于私有字段而言，getter和setter方法也是私有的，则该字段只能在类内部使用
  *     3. 生成的set 方法是没有带括号的，所以你在使用的时候也不能带括号，所以一切看起开都像是在直接调用属性（在私有属性的方法也是私有的之后更像了）
  *     4. 如果字段是val，则只会生成getter方法
  *     5. 如果你不需要任何getter和setter方法，可以将字段声明为private[this]
  *  4. 构造器
  *     1. 主构造器
  *         1. 每个类都有一个主构造器。主构造器并不以this方法定义，而是与类定义交织在一起
  *         2. 主构造器的参数被编译成字段，其值被初始化成构造时传入的参数(默认使用val 修饰的)
  *         3. 主构造器会执行类定义中的所有语句
  *     2. 辅助构造器
  *         1. 辅助构造器的名称为this
  *         2. 每个辅助构造器都必须以一个对先前已经定义的其他辅助构造器或者主构造器的调用开始。所以主构造器是所有辅助构造器的根源。
  */
class Counter {
    /**
      * 发现这里不支持使用public来修饰变量，因为public 是默认的，所以就不用修饰
      */
    private var value = 0;
    var name = "kingcall"

    def incerece(): Unit = {
        value += 1
    }

    def current = value

    def get() = value

    def setValue(x: Int): Unit = {
        println(s"在内部使用对象的私有字段的set方法,设置后的值是：${x}")
        value_$eq(x)
    }
}

class ThisCounter {
    /*
    * private[this]  将不会生产 getter setter方法
     */
    private[this] var value = 0;

    def incerece(): Unit = {
        value += 1
    }

    def current = value

    def setValue(x: Int): Unit = {
        println(s"在内部使用对象的私有字段的set方法,设置后的值是：${x}")
        // value_$eq(x) 发现编译不通过
    }
}

/**
  * 将生成 getValue   setValue(10) 方法 和java的操作保持类似
  */
class GetSetCounter {
    @BeanProperty var value: Int = 0;

    def incerece(): Unit = {
        value += 1
    }
}

class Student(var age:Int,var name:String) {
    println("主构造器被调用,")
    def this(name: String) {
        // 调用主构造器，即使没有定义有参数的主构造器
        this(24,"KINGCALL")
        println(s"第一辅助构造器被调用,改变了名字:${name}")
        this.name = name
    }
    def this(age: Int) {
        this("KINGCALL") // 调用前一个辅助构造器
        println(s"第二辅助构造器被调用,改变了年龄:${age}")
        this.age = age
    }

    override def toString: String = s"年龄：${age},姓名：${name}"
}

class Person {
    private var name = ""
    private var age = 0
    println("主构造器被调用")

    def this(name: String) {
        // 调用主构造器，即使没有定义有参数的主构造器
        this()
        println("第一辅助构造器被调用")
        this.name = name
    }
    def this(name: String, age: Int) {
        this(name) // 调用前一个辅助构造器
        println("第二辅助构造器被调用")
        this.age = age
    }
}
class class1(name: String, age: Int) {
        println(s"name：${name},age:${age}")

        def showinfo(): Unit = {
            println(s"name：${name},age:${age}")
        }

        override def toString: String = s"name：${name},age:${age}"

}


    object test {

        def test1(): Unit = {
            val obj = new class1("kingcall", 22)
            obj.showinfo()
            println(obj)
        }

        /**
          * 针对字段自动生成的get set方法
          *
          */
        def test2(): Unit = {
            val counter = new Counter()
            println(counter.current)
            counter.incerece()
            println(counter.current)
            println(counter.get)
            // 将调用 counter.name() 方法
            println(counter.name)
            // 将调用 counter.name_() 但其实字节码中是counter.name_$eq()方法
            counter.name = "刘备"
            println(counter.name)
            // 你会发现这个方法最会会指向属性的设置,但是私有属性不允许这样调用
            counter.name_$eq("刘文强")
            println(counter.name)
            counter.setValue(10)
            println(counter.current)
        }

        /**
          * 生成类似 java 字段的get set 方法
          */
        def test3(): Unit = {
            val counter = new GetSetCounter
            counter.setValue(100)
            println(counter.value)
        }

        /**
          * private[this] 修饰符的效果展示
          */
        def test4(): Unit = {
            val counter = new ThisCounter
            counter.setValue(10)
            println(counter.current)
        }

        def test5(): Unit ={
            val p1 = new Person
            println("==============================================")
            val p2 = new Person("Fred")
            println("==============================================")
            val p3 = new Person("Fred", 23)
            println("==============================================")
        }
        def test6(): Unit ={
            val p1 = new Student(24,"kingcall")
            println(p1)
            println("==============================================")
            val p2 = new Student("Fred")
            println(p2)
            println("==============================================")
            val p3 = new Student(23)
            println(p3)
            println("==============================================")
        }

        def main(args: Array[String]): Unit = {
            test6()
        }
    }





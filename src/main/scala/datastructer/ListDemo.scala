package scala.collection

import scala.annotation.tailrec
import scala.collection.mutable._

/**
  *  1. scala 的 list 是协变的 ,所以 val xl:List[String]=List[Nothing]()
  *  2. Nothing 是所有类型的子类型
  *  3. 判断列表是否为空   x.isEmpty(推荐使用)         x.length==0 (不推荐使用)
  *  4. drop 除去前几个    take() 去前几个
  */
object ListDemo {

    def main(args: Array[String]): Unit = {
        high_op
    }

    /**
      * 基本概念的理解
      */
    def baseConcept(): Unit = {
        println(List[Nothing]() == List())
        println(List() == Nil)
        val xl: List[String] = List[Nothing]()
        println(xl)
    }

    /*测试list的基本属性  Nil  空列表   尾部永远是一个列表*/
    def base_struct(): Unit = {
        val digits = List(1, 2, 3)
        println(digits.head)
        println(digits.tail.mkString("\t"))

        /*:: 操作符从你给的头和尾部创建一个列表  沪江尾部的元素提取出来，但不会将头部的元素提取出来，所有你最好保证头部是一个元素，而不是一个列表*/
        val p1 = 9 :: List(1, 2, 3)
        println(p1.isInstanceOf[List[Int]])
        println(p1.isInstanceOf[List[String]])
        println(p1.mkString("\t"))

        val p2 = List(1, 2, 3) :: List(4, 5, 6) //List(1, 2, 3)	4	5	6
        println(p2.mkString("\t"))
        /*注意一下后面的那个  Nil 不能少，从list的定义去理解*/
        println(sumlist(1 :: 10 :: Nil))
        println(List(1, 2, 3) == 1 :: 2 :: 3 :: Nil)
        println(List(1, 2, 3) == 1 :: (2 :: (3 :: Nil)))
    }

    def base_op(): Unit = {
        val s = List(1, 2, 3, 4, 5)
        // 取列表头部第一个元素
        println(s.head)
        // 取除过头部的元素形成的列表
        println(s.tail)
        // 取列表的最后一个元素
        println(s.last)
        // 取除去最后一个元素，形成的头部列表
        println(s.init)
        // 取前多少个元素组成的集合，可以形成 init
        println(s.take(s.size - 1))
        // 除去前几个元素的列表
        println(s.drop(1))
        println(s.drop(1) == s.tail)
        // 列表模式使用的两种方式  可以完成一次对多个变量的赋值（获取列表的特定元素）  还可以完成对
        val List(a, b, c, _*) = s
        val a1 :: b1 :: c1 :: tail = s
        println((a, b, c), (a1, b1, c1))
        println(tail)
        // :+ 向集合尾部添加元素  +:向集合头部添加元素   （：永远靠近集合  集合在前往集合尾部添加元素，集合在后往集合头部添加元素）
        val list = "a" +: "b" +: Nil
        val list2 = Nil :+ "a" :+ "b"
        // 列表的拼接 和 :: 一样也是右连接
        println(List(1,2,3):::List("a","b","c"))
        // 反转操作   如果需要频繁地使用list 末尾的元素  建议将列表反转进行操作（遍历列表是个很费时间的操作）
       println(s== s.reverse.reverse)
    }

    /**
      * 1.  zip 和 unzip 可以对多个列表进行操作
      * 2.  mkstring() 方法的重载操作
      * 3.
      */
    def super_op(): Unit = {
        val s = List(1, 2, 3, 4, 5)
        println(s.indices)
        println(List(List(1, 2, 3), List("a", "b", "c")).flatten)
        val s2 = s.indices.toList
        val zips = s zip s2
        println(zips.mkString("\t"))
        println(s.zipWithIndex.mkString("\t"))
        println(s apply 3)
        // apply 是借助 drop() 完成的
        println(s.drop(3).head)
        println(s.mkString("<<","\t",">>"))
        println(s.iterator.next())
        println(s.toArray.mkString("="))
        val x=new Array[Int](3)
        s.copyToArray(x)
        println(x.mkString("="))
    }

    def high_op(): Unit ={

    }

    /*根据特性来遍历*/
    def sumlist(stl: List[Int]): Int = {
        if (stl == Nil) 0 else stl.head + sumlist(stl.tail)
    }

    def Functiondemo(): Unit = {
        val p = List(1, 2, 3, 4, 5, 6, 7, 8)
        println(p.reduceLeft(_ - _))
        println(p.reduceRight(_ - _))
        /*默认的Reduce就是从左到右，也就是reduceRight
        * */
        println(p.reduce(_ - _))

        val p2 = List(1, 2, 3, 4, 5)
        /*中间那个参数的意思实在没有搞懂，好像填什么都行的*/
        p.zipAll(p2, 'r', "a").foreach(x => println(x))

        /*缓存型迭代器   可以先让你判断一下元素，再决定是否消费*/
        val pit = p2.iterator.buffered
        while (pit.hasNext && pit.head.%(2) == 0) {
            println("====================" + pit.next())
        }
    }
    def append[T](x:List[T],y:List[T]):List[T]={
        x match {
            case List()=>y
            case x1::xrest=>x1::append(xrest,y)
        }

    }


}

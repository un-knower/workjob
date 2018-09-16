package matchdemo

import org.apache.spark.sql.catalyst.expressions.Pi
import redis.clients.jedis.Tuple

/**
  *     1. 带类型的模式往往会把变量保存下来
  *     2. 守卫模式往往和变量模式搭配
  *     3. 通用匹配 _ 和变量匹配可以等同(不是类型匹配)
  * 4.
  */
object MatchLearn {

    def main(args: Array[String]): Unit = {
        variableBound_test
    }

    // 常量模式
    def test1(x: Any) = x match {
        case 5 => {
            println(x); x
        }
        case true => {
            println(x); x
        }
        case _ => println("maybe others")
    }

    // 变量模式
    def test2(x: Any) = x match {
        case 5 => {
            println(x); x
        }
        case true => {
            println(x); x
        }
        case elsething => {
            println("进入变量匹配模式")
            if (elsething.toString.contains("king")) "king" else "call"
        }
    }

    /**
      * 直接在case语句后面接类构造器，匹配的内容放置在构造器参数中
      *
      * @param expr
      */
    def test3(expr: Expr) = expr match {
        case BinOp("+", Number(1), Number(0)) => println("深度匹配")
        case _ => println("没有匹配上")
    }

    /**
      * 　序列模式用于匹配如数组Array、列表List、Range这样的线性结构集合，其实原理也是通过case class起作用的
      *
      * @param list
      */
    def test4(list: List[Any]) = list match {
        case List(0, _, 0) => println("三个元素以 0 开头，以 0 结尾")
        case List(0, _, _) => println("三个元素以 0 开头")
        case List(0, _*) => println("以 0 开头,任意多个元素")
        case List(_*) => println("无限制的list")
    }

    // 元祖模式
    def tupledemo(x: Any) = x match {
        case ("a", b, c) => println(s"a 开头的三元组 a,${b},${c}")
        case (Pi, b, c) => println(s"Pi 开头的三元组 ${Pi},${b},${c}")
    }

    // 带类型的模式
    def classdemo(x: Any) = x match {
        case s: String => {
            println("字符串"); s.length
        }
        case m: Map[String, String] => println("是数组")
        case x: Any => println("任意模式")
    }

    // 守卫模式
    def guard(x: Any) = x match {
        case s: String if (s.length > 3) => {
            println("字符串"); s.length
        }
        case m: Map[String, String] => println("是数组")
        case x: Any => println("任意模式")
    }

    def variableBound_test(): Unit = {
        var t = List(List(1, 2, 3), List(2, 3, 4,5))
       println( variableBound(t))
    }

    /**
      * 在进行模式匹配时，有时不仅仅只是返回一个变量，也可以将某个变量绑定到某个模式上。从而将整体匹配结果赋值给该变量。
      * 对变量进行了再一次的约束
      */
    def variableBound(t: Any) = t match {
        // 将变量绑定到了 List(_,_,_)上,其实就是拿变量e 再次进行了匹配，看e 是否满足  List(_,_,_)
        case List(_, e@List(_, _, _)) => e
        case _ => Nil
    }
}

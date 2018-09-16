package scala.implicitdemo

import java.io.File

import scala.io.Source

object ClassDemo {
    object Context {
        implicit val ccc: String = "implicit"
    }

    object Param {
        def print(content: String)(implicit prefix: String) {
            println(prefix + ":" + content)
        }
    }
    def main(args: Array[String]) {
        Param.print("jack")("hello")
        import Context._
        // 使用了外部对象的隐式变量
        Param.print("jack")
    }


}

/**
  * 普通方法两步走
  *
  * @param file
  */
class RichFile(val file: File) {
    println("隐式转换函数被调用")
    def read = Source.fromFile(file).getLines().mkString
}

object ImplicitRead {
    def main(args: Array[String]): Unit = {
        //隐式函数将java.io.File隐式转换为RichFile类 因为 File类中并不存在 read方法
        implicit def file2RichFile(file: File) = new RichFile(file)
        val f = new File("C:\\Users\\PLUSH80702\\Desktop\\data.txt").read
        println(f)
    }
}

/**
  * 高级方案一步走，利用了隐式class,将一个类转换成了另一个类，然后调用起方法
  *
  */
object ImplicitRead2 {

    /*更高级的做法，避免了多余对象的构造，而且没有了隐式函数的书写，将隐式类的构造函数当成了隐式函数*/
    implicit class RichFile(val file: File) extends AnyVal {
        def read = Source.fromFile(file).getLines().mkString
    }

    //隐式函数将java.io.File隐式转换为RichFile类 因为 File类中并不存在 read方法，转换接收对象
    def main(args: Array[String]): Unit = {
        val f = new File("C:\\Users\\PLUSH80702\\Desktop\\data.txt").read
        println(f)
    }
}


object Rect{

    /**
      * 隐式类的精髓在于能生成一个隐式的方法（利用构造方法的参数），生成当前隐式类的对象，然后去调用隐式类的其他方法
      * @param width
      * @param height
      */
    case class Rectangle(width:Int,height:Int)
    implicit class RectangleMaker(width:Int){
        def x(height:Int)=new Rectangle(width,height)
    }
    def main(args: Array[String]): Unit = {
        println(3 x 4)
    }
}


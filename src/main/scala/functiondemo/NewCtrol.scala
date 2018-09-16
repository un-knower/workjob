package functiondemo

import java.io.{File, PrintWriter}

object NewCtrol {

  def main(args: Array[String]): Unit = {
    test1()
  }
  def withPrinter(file:File,op:PrintWriter=>Unit): Unit ={
    val writer=new PrintWriter(file)
    try {
      op(writer)
    }finally {
      writer.close()
    }
  }
  def withPrinter_curry(file:File)(op:PrintWriter=>Unit): Unit ={
    val writer=new PrintWriter(file)
    try {
      op(writer)
    }finally {
      writer.close()
    }
  }

  def test1(): Unit ={
    //这里穿进去的是函数字面量
    withPrinter(new File("D:/test.txt"),witer=>witer.println("kingcall"))
    def te(writer: PrintWriter): Unit ={
      writer.println("kingcall")
    }
    //打包成函数字面量
    withPrinter(new File("D:/test.txt"),te _)

    val te2=(writer:PrintWriter)=>writer.println("kingcall")
    withPrinter(new File("D:/test.txt"),te2)

  }
  //花括号仅可以再在一个参数时使用
  def op_curry(): Unit ={
    withPrinter_curry(new File("D:/test.txt")){
      // 可以在写函数字面量的时候加上参数
      witer:PrintWriter=>witer.println("kingcall")
    }
    withPrinter_curry{new File("D:/test.txt")}{
      witer:PrintWriter=>witer.println("kingcall")
    }


  }

}

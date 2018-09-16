package exception

import java.io.{FileNotFoundException, FileReader}
import java.lang.ArrayIndexOutOfBoundsException

/**
  * @program: workjob
  * @description: ${description}
  * @author: 刘文强 kingcall
  * @create: 2018-08-01 22:01
  **/
object catchdemo {
  def main(args: Array[String]): Unit = {
    test1()
  }

  def test1(): Unit ={
    try{
      val f=new FileReader("dd")
    }catch {
      case ex:FileNotFoundException=>println("文件未找到")
      case ex:Exception=>println("其他错误")
    }finally {
      println("程序执行完毕")
    }

  }

}

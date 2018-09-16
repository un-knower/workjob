package functiondemo

import scala.io.Source

/**
  * @program: workjob
  * @description: ${description}
  * @author: 刘文强 kingcall
  * @create: 2018-08-01 22:42
  **/
object LongLines {
  def main(args: Array[String]): Unit = {
    processFile2("D:\\logs\\controller.log",40)
  }
  def processFile(filename:String,width:Int): Unit ={
    val souces=Source.fromFile(filename)
    for(line<-souces.getLines()){
      processLine(filename,width,line)
    }
  }
  private def processLine(filename:String,width:Int,line:String): Unit ={
    if(line.length>width){
      println(filename+":"+line.trim)
    }
  }

  /**
    * 对以上方法的重构
    * @param filename
    * @param width
    */
  def  processFile2(filename:String,width:Int): Unit = {
    val souces = Source.fromFile(filename)
    for (line <- souces.getLines()) {
      processLine(line)
    }

    /**
      * 可以直接使用外部函数的参数
      * @param line
      */
    def processLine(line: String): Unit = {
      if (line.length > width) {
        println(filename + ":" + line.trim)
      }

    }
  }

}

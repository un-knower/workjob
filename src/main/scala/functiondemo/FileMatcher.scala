package functiondemo

import java.io.File

/**
  * 这个代码值得好好体会
  */
object FileMatcher {
  def main(args: Array[String]): Unit = {
    filesEnd2(".log")
  }
  private def files=(new File("D:\\logs")).listFiles()
  def filesEnd(query:String)=for(file<-files;if(file.getName.endsWith(".scala"))) yield file
  def filesContain(query:String)=for(file<-files;if(file.getName.contains("kingcall"))) yield file
  def filesRegex(query:String)=for(file<-files;if(file.getName.matches("kingcall"))) yield file
    // 看到了打包的强大之处，函数变形(而且不变形的话就会很繁琐)
  def filesEnd2(query:String)=
    filesMatching(query,_.endsWith(query))

  def filesMatching(query:String,matcher:(String)=>Boolean)={
    for(file<-files;if matcher(file.getName)) yield file
  }



}

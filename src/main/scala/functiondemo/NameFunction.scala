package functiondemo

/**
  * @program: workjob
  * @description: ${description}
  * @author: 刘文强 kingcall
  * @create: 2018-08-03 08:04
  **/

class NameFunction {
  val enavle=true


  def main(args: Array[String]): Unit = {
    myassert(()=>true)
    //函数字面量
    myassert(()=>5>3)
    myassert_name(5>3)
  }
  //可以看到下面的过程其实一直在使用（） 不使用的时候只是只有一个参数的时候
  def myassert(f:()=>Boolean): Unit ={
    if(enavle&&f()) throw new AssertionError("error")

  }
  //传名参数实现，注意空格
  def myassert_name(f: =>Boolean): Unit ={
    if(enavle&& f) throw new AssertionError("error")

  }


  def myassert2(f:String=>Boolean): Unit ={

  }
  def myassert_2(f:(String)=>Boolean): Unit ={

  }
  def myassert3(f:(String,String)=>Boolean): Unit ={
  }
}

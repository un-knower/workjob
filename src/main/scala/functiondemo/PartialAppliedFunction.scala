package functiondemo

/**
  * @program: workjob
  * @description: ${description}
  * @author: 刘文强 kingcall
  * @create: 2018-08-04 11:11
  **/
object PartialAppliedFunction {
  def main(args: Array[String]): Unit = {
    val result = sum _
    val r = result(2)(3)(4)
    println(r)
  }

  def sum(i: Int)(j: Int)(k: Int): Int ={
    i + j + k
  }
}

package exception

/**
  * @program: workjob
  * @description: ${description}
  * @author: 刘文强 kingcall
  * @create: 2018-08-01 21:58
  **/
object throwdemo extends App {
  val n=7
  val s=if(n%2==0) n/7 else throw new RuntimeException("n must be even")
  println(s)

}

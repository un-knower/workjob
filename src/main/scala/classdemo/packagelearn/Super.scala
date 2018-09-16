package classdemo.packagelearn

/**
  * @program: workjob
  * @description: ${description}
  * @author: 刘文强 kingcall
  * @create: 2018-08-04 10:42
  **/
class Super {
   def f()={println("hello kingcall")}
}
class sub extends Super{
  f()
}
class Other{
  // 无法访问

}
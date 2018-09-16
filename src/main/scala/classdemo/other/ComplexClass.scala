package classdemo.other

/**
  * 复合类型
  *     复合类型通过继承、混入来实现，
  */
object ComplexClass {
    class A
    class B extends A with Cloneable
    type X=A with Cloneable
    def test(x:X)=println("test ok")
    def main(args: Array[String]): Unit = {
        // 可以看出类型 X是和 B 相同的
        test(new B)
        println()
    }
}

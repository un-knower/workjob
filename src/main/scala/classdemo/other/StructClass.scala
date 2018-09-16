package classdemo.other

/**
  * 结构类型（Struture Type）通过利用反射机制为静态语言添加动态特性，
  *     1. 直接使用的方式（直接 new 的方式）
  *     2. type 定义的方式
  *     3. class 的使用方式
  *     4. 单例对象的使用方式
  */

object  StructClass {
    // 利用type 关键字定义类型
    type X={def close():Unit}
    // 结构体类型其实可以看作是一个类，在函数调用时，直接通过new操作来创建一个结构体类型对象，当然它是匿名的。因此，上述方法也可以传入一个实现了close方法的类或单例对象
    class File{
        def close():Unit=println("File Closed")
    }
    //定义一个单例对象，其中也包含了close成员方法
    object File{
        def close():Unit=println("object File closed")
    }

    def main(args: Array[String]): Unit = {
        struct2()
    }
    def struct1(): Unit ={
        releaseMemory(new {def close()=println("closed")})
    }

    def struct2()={
        releaseMemory2(new {def close()=println("closed2")})
        releaseMemory(File)
        releaseMemory(new File())
    }
    // 利用type 关键字实现结构类型
    def releaseMemory2(x:X){
        x.close()
    }

    // 这个方法的参数就是一个结构类型
    def releaseMemory(res:{def close():Unit}){
        res.close()
    }

}

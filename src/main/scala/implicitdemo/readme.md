[TOC]
# 隐式转换
  ~~~
  库函数只能使用,不许修改,所以scala提供了隐式转换和隐式参数，来达到扩展库功能
  可以减少代码量，不用像普通方法那样每次都去显式的调用
  ~~~
## 使用方法
   - 将方法或变量标记为implicit  
       ~~~
      隐式转换函数的函数名可以是任意的，与函数名称无关，只与函数签名（函数参数和返回值类型）有关
      如果当前作用域中存在函数签名相同但函数名称不同的两个隐式转换函数，则在进行隐式转换时会报错
      ~~~
   - 将方法的参数列表标记为implicit
       ~~~
       当函数没有柯里化时，implicit关键字会作用于函数列表中的的所有参数。
       隐式参数使用时要么全部不指定，要么全不指定，不能只指定部分，也就是只能出现在参数列表的最前面
       柯里化的函数， implicit 关键字只能作用于最后一个参数
       implicit 关键字在隐式参数中只能出现一次，柯里化的函数也不例外！
       ~~~
   - 将类标记为implicit    
      >其所带的构造参数有且只能有一个  
      隐式类必须被定义在类，伴生对象和包对象里  
      隐式类不能是case class（case class在定义会自动生成伴生对象与2矛盾）  
      在某些特定的环境可以达到和隐式方法效果一样的效果，但是不能取代隐式方法
### 原则  
   -  无歧义规则
   - 一次性转换规则
        
package classdemo.traitlearn


/**
  * 特质继承了抽象类
  * 方法用了  abstract override 可叠加修改
  */
trait Doubling extends IntQueue{
  abstract override def put(x: Int) = {super.put(2*x)}
}
trait Incrementing extends IntQueue{
  abstract override def put(x: Int): Unit = super.put(x+1)
}
trait Filtering extends IntQueue{
 abstract override def put(x: Int): Unit = {
    if(x>=0) super.put(x)
  }
}

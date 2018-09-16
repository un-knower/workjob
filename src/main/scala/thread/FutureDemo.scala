package thread

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration.Duration
import scala.concurrent.{Await, Future, Promise}
import scala.util.{Failure, Success}
import org.scalatest.Matchers._
import org.scalatest.concurrent.ScalaFutures._

object FutureDemo {
  def main(args: Array[String]): Unit = {
    text_op()
  }

  /**
    * 测试
    */
  def text_op(): Unit ={
    val success=Future{Thread.sleep(1000);20}
    // 阻塞后获得的结果可以用来测试
    val x=Await.result(success,Duration.Inf)
    println(x)
    x shouldBe(20)
    println("下面将会阻塞，直到结果完成")
    val success2=Future{Thread.sleep(100);20}
    // 阻塞到future完成，优势是你不用写等待多长时间
    success2.futureValue.shouldBe(20)
    // 异步测试，异步返回测试的结果
    val success3=Future{Thread.sleep(1000);20}
    val result=success3 map(sum=>assert(sum==30))
    Thread.sleep(1000)
    println(result.value)
  }

  /**
    * 在future执行结束后执行，相当于副作用
    */
  def other_action(): Unit ={
    val success=Future{20}
    val failure=Future(20/0)
    Thread.sleep(1000)
    success.foreach(ex=>println("成功执行结束"))
    println(success.value)
    failure.foreach(ex=>println("失败执行结束"))
    println(failure.value)
    success onComplete{
      case Success(res)=> println(res)
      case Failure(ex)=> println(ex)
    }
    failure onComplete{
      case Success(res)=> println(res)
      case Failure(ex)=> println(ex)
    }
    success andThen {
      case Success(res)=> println(res)
      case Failure(ex)=> println(ex)
    }

  }
  /**
    * 针对future 的高级操作
    */
  def high_op(): Unit ={
    val success=Future{20}
    val recover=Future(20/0) recover{
      case ex:ArithmeticException => 20
    }
    val failure=Future(20/0)
    // 组合操作
    val zipsucess= success zip recover
    val zipfaild= success zip failure
    // fold 操作 还有对应的reduce操作
    val foldsucess=List(success,recover)
    val foldfaild=List(failure,recover)
    val foldedsucess=Future.fold(foldsucess)(0){
      (acc,num)=>{acc+num}
    }
    val foldedfaild=Future.fold(foldfaild)(0){
      (acc,num)=>{acc+num}
    }
    val reduce=Future.reduce(foldsucess){
      (acc,num)=>{acc+num}
    }
    // 转换操作 将 future的集合 转化成 future[list]
    val sequence=Future.sequence(foldsucess)
    Thread.sleep(2000)
    println(zipsucess.value)
    println(zipfaild.value)
    println(foldedsucess.value)
    println(foldedfaild.value)
    println(reduce.value)
    println(sequence.value)

  }

  /**
    * 针对失败的一些情况进行处理
    */
  def deal_op(): Unit ={
    val failure=Future(1/0)
    val success=Future(1)
    // 提供一个额外的 future 在失败的是有被调用，如果候补也失败了，将返回原始的错误
    val fallback=failure.fallbackTo(success)
    Thread.sleep(500)
    println(fallback.value)
    // 在失败的时候进行恢复 恢复成一个特定的值
    val recover=failure.recover({
      case ex:ArithmeticException => -1
    })
    Thread.sleep(1000)
    println(recover.value)
    // 恢复成一个future
    val recover2=failure.recoverWith({
      case ex:ArithmeticException => Future(-1)
    })
    val failure2=Future(1/0)
    // transform 接收两个方法第一个用于处理成功地，第二个用于处理失败的
    val value=failure2.transform(_+1,ex=> new Exception("some error occur"))
    println(value.value)
    // scala 2.12才支持
   /* val value2=failure.transform{
      case Success(res)=> Success(res+10)
      case Failure(ex)=>
        Failure(new Exception("some thing is error"))
    }*/

  }
  def base_op(): Unit ={
    val fut=Future{Thread.sleep(2000);20}
    val valid=fut.filter(res=>res>0)
    println(valid.value)
    Thread.sleep(3000)
    println(valid.value)
    val fut2=Future(30)
    val valid2=for(res<-fut2 if res>0) yield res
    println(valid2.value)
    Thread.sleep(1000)
    println(valid2.value)
    // collect 需要一个偏函数  完成对结果的校验与更新
    val fut3=Future(40).collect{case res if res>0 => res+60}
    Thread.sleep(1000)
    println(fut3.value)
  }
  /**
    * 用 Promise 创建future,然后控制future
    */
  def promise(): Unit ={
    val pro=Promise[Int]
    val fut=pro.future
    println(fut.value)
    pro.success(42)
    println(fut.value)
    val pro2=Promise[Int]
    val fut2=pro2.future
    pro2.failure(new Exception("some error occur"))
    println(fut2.value)
    val pro3=Promise[Int]
    val fut3=pro3.future
    pro3.complete(Success{20})
    println(fut3.value)
    // 还有一些的try 方法，对应着 success failure  complete 参数一样只不过返回值变了,其实上面方法的底层都是调用的complete()方法，而complete() 调用 tryComplete对应的方法
    val pro4=Promise[Int]
    val fut4=pro4.future
    val result=pro4.tryComplete(Success{20})
    println(result)
    //
    val pro5=Promise[Int]
    val fut5=pro5.future
    val result5=pro5.completeWith(Future{Thread.sleep(3000);101})
    Thread.sleep(4000)
    println(fut5.value)
    println(result5)
  }
  //创建已完成的future对象
  def test3(): Unit ={
    val sucess=Future.successful(20+20)
    val faild=Future.failed(new Exception("some error occur"))
    val `try`=Future.fromTry(Success{21+21})
    val failure=Future.fromTry(Failure(new Exception("oh my gold")))
    println(sucess)
    println(faild)
    println(`try`)
    println(failure)
  }
  def test2(): Unit ={
    val x=Future{
      Thread.sleep(2000);20
    }
    val y=Future{
      Thread.sleep(2000);30
    }
    Thread.sleep(4000)
    val sum=for{x1<-x ;y1<-y} yield x1+y1
    println(sum)
  }
  def test1(): Unit ={
    val fut=Future{
      Thread.sleep(2000);20+21
    }
    println(fut)
    println(fut.value)
    val result1=fut.map(x=>x+1)
    println(result1)
    println(result1.value)
    Thread.sleep(3000)
    println(result1.value.get.get)
  }

}

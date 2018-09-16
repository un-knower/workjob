package thread

import akka.actor.{Actor, Props}


/**
  * Scala提供了Actor trait来让我们更方便地进行actor多线程编程，就Actor trait就类似于Java中的Thread和Runnable一样，是基础的多线程基类和接口。
  * 我们只要重写Actor trait的act方法，即可实现自己的线程执行体，与Java中重写run方法类似。
  * 此外，使用start()方法启动actor；使用!符号，向actor发送消息；actor内部使用receive和模式匹配接收消息
  * 从Scala的2.11.0版本开始，Scala的Actors库已经过时了。早在Scala2.10.0的时候，默认的actor库即是Akka。
  */
/**
  * Actor模型允许我们从信息交流的方式去考虑我们的代码，而不像传统编程中类似大型组织中的人员互相交流
  * 在协作的环境下，通过发送信号来改变协作实体的状态。通过相互发送信息来推动整个用应用的运行。
  * akka里的actor总是属于其父母。通常你通过调用 context.actorOf() 创建一个actor。这种方式向现有的actor树内加入了一个新的actor
  */
object ActorDemo {
  class PrintMyActorRefActor extends Actor {
    override def receive: Receive = {
      case "printit" ⇒
        val secondRef = context.actorOf(Props.empty, "second-actor")
        println(s"Second: $secondRef")
    }
  }
  def test1(): Unit ={

  }

}

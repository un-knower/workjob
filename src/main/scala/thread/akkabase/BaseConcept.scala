package thread.akkabase

import akka.actor.{ActorSystem, Props}

import scala.AkkaDM.helloworld.BetterMaster


object BaseConcept {
  def main(args: Array[String]): Unit = {

  }
  def test1(): Unit ={
    val system = ActorSystem("HelloActors")
    system.actorOf(Props[BetterMaster], "master")
  }

}

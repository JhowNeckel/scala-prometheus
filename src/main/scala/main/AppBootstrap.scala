package main

import akka.actor.typed.ActorSystem
import server.HttpServer
import server.HttpServer.GracefulShutdown

object AppBootstrap extends App {

  val system = ActorSystem(HttpServer(), "http-server")

  sys.addShutdownHook {
    system ! GracefulShutdown
  }

}

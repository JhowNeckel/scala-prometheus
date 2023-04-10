package server

import akka.actor.typed.scaladsl.Behaviors
import akka.actor.typed.{ActorSystem, Behavior}
import akka.http.scaladsl.Http
import akka.http.scaladsl.Http.ServerBinding
import inj.AppModule
import io.prometheus.client.exporter.HTTPServer
import routes.MainRoutes

import scala.concurrent.Future
import scala.util.{Failure, Success}

object HttpServer {

  sealed trait Command

  case object GracefulShutdown extends Command

  private case class ServerStarted(binding: ServerBinding) extends Command

  private case class ServerFailure(e: Throwable) extends Command

  def apply(): Behavior[Command] = Behaviors.setup { context =>
    implicit val system: ActorSystem[_] = context.system
    implicit val module: AppModule = AppModule()

    context.pipeToSelf(bind("0.0.0.0", 8000)) {
      case Success(binding) => ServerStarted(binding)
      case Failure(e) => ServerFailure(e)
    }

    def connected(binding: ServerBinding): Behavior[Command] = Behaviors.setup { _ =>
      module.log.info(this, "server started", binding.localAddress.toString)

      new HTTPServer.Builder()
        .withInetAddress(binding.localAddress.getAddress)
        .withPort(8001)
        .withRegistry(module.registry)
        .build()

      Behaviors.receiveMessage {
        case ServerFailure(_) => Behaviors.stopped
        case GracefulShutdown => Behaviors.stopped
        case _ => Behaviors.stopped
      }
    }

    Behaviors.receiveMessage {
      case ServerStarted(binding) => connected(binding)
      case ServerFailure(_) => Behaviors.stopped
      case GracefulShutdown => Behaviors.stopped
    }
  }

  private def bind(host: String, port: Int)(implicit system: ActorSystem[_], module: AppModule): Future[ServerBinding] =
    Http().newServerAt(host, port).bind(MainRoutes().routes)

  override def toString: String = getClass.getSimpleName
}

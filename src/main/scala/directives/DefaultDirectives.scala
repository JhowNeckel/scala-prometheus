package directives

import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.{Directive0, ExceptionHandler}
import inj.AppModule

trait DefaultDirectives extends MetricsDirectives {
  val module: AppModule

  def baseProcesses: Directive0 =
    exceptionHandler.wrap(metricsProcessor)

  private def exceptionHandler: Directive0 = {
    extractLog flatMap { logger =>
      handleExceptions {
        ExceptionHandler { case e =>
          logger.error(e.getMessage)
          module.errors.inc()
          module.inprogress.dec()
          complete(StatusCodes.InternalServerError)
        }
      }
    }
  }

}

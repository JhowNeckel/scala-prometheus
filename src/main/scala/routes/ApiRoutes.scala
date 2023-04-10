package routes

import akka.http.scaladsl.model.{ContentTypes, HttpEntity, HttpRequest, StatusCodes}
import akka.http.scaladsl.server.Directives.{complete, get, path}
import akka.http.scaladsl.server.{Route, RouteResult}
import directives.DefaultDirectives
import inj.AppModule
import util.Conversions._

import scala.util.Random

class ApiRoutes()(implicit val module: AppModule) extends DefaultDirectives {

  override def metricRequest: HttpRequest => Unit = { _ => module.inprogress.inc() }

  override def metricResponse(req: HttpRequest): RouteResult => Unit = { result =>
    result match {
      case RouteResult.Complete(resp) => module.requests.labels(resp.statusCode, req.path, req.methodValue).inc()
      case RouteResult.Rejected(_) =>
    }

    module.inprogress.dec()
  }

  def routes: Route = baseProcesses {
    path("api") {
      val requestTimer = module.requestLatency.startTimer()
      get {
        module.log.info(this, "received hello world")
        Thread.sleep(Random.nextInt(10) * 500)
        if (Random.nextBoolean()) {
          requestTimer.observeDuration()
          complete(HttpEntity(ContentTypes.`text/html(UTF-8)`, "<h1>Say hello to akka-http</h1>"))
        } else {
          requestTimer.observeDuration()
          complete(StatusCodes.BadRequest)
        }
      }
    }
  }

  override def toString: String = getClass.getSimpleName

}

object ApiRoutes {
  def apply()(implicit module: AppModule): ApiRoutes = new ApiRoutes()
}
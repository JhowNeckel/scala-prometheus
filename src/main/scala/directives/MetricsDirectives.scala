package directives

import akka.http.scaladsl.model.HttpRequest
import akka.http.scaladsl.server.directives.{DebuggingDirectives, LoggingMagnet}
import akka.http.scaladsl.server.{Directive0, RouteResult}

trait MetricsDirectives {

  def metricRequest: HttpRequest => Unit
  def metricResponse(request: HttpRequest): RouteResult => Unit

  def metricsProcessor: Directive0 =
    DebuggingDirectives.logRequest(LoggingMagnet(_ => metricRequest))
      .wrap(DebuggingDirectives.logRequestResult(LoggingMagnet(_ => metricResponse)))

}

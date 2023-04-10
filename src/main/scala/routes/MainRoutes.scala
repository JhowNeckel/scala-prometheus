package routes

import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import inj.AppModule

class MainRoutes()(implicit module: AppModule) {

  private val apiRoutes = ApiRoutes()
  private val metricsRoutes = MetricsRoutes()

  val routes: Route = concat(
    apiRoutes.routes,
    metricsRoutes.routes
  )

  override def toString: String = getClass.getSimpleName

}

object MainRoutes {
  def apply()(implicit module: AppModule): MainRoutes = new MainRoutes()
}

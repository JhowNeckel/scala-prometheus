package routes

import akka.http.scaladsl.model.{ContentTypes, HttpEntity, HttpResponse, StatusCodes}
import akka.http.scaladsl.server.Directives.{complete, extractUri, path}
import akka.http.scaladsl.server.Route
import inj.AppModule
import io.prometheus.client.SampleNameFilter
import io.prometheus.client.exporter.common.TextFormat

import java.io.{ByteArrayOutputStream, OutputStreamWriter}
import java.util

class MetricsRoutes()(implicit module: AppModule) {

  def routes: Route = path("metrics") {
    extractUri { uri =>
      val names = new util.HashSet[String]
      val stream = new ByteArrayOutputStream()
      val writer = new OutputStreamWriter(stream)

      uri.query().toMultiMap.get("name").foreach(_.foreach(names.add))

      val filter = SampleNameFilter.restrictToNamesEqualTo(null, names)

      if (filter == null) TextFormat.write004(writer, module.registry.metricFamilySamples())
      else TextFormat.write004(writer, module.registry.filteredMetricFamilySamples(filter))

      writer.close()

      complete(HttpResponse(StatusCodes.OK, entity = HttpEntity(ContentTypes.`text/plain(UTF-8)`, stream.toByteArray)))
    }
  }

  override def toString: String = getClass.getSimpleName

}

object MetricsRoutes {
  def apply()(implicit module: AppModule): MetricsRoutes = new MetricsRoutes()
}
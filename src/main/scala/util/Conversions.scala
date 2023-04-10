package util

import akka.http.scaladsl.model.{HttpRequest, HttpResponse}

object Conversions {

  implicit class HttpRequestUtils(request: HttpRequest) {
    def path: String = request.uri.path.toString()
    def methodValue: String = request.method.value
  }

  implicit class HttpResponseUtils(response: HttpResponse) {
    def statusCode: String = response.status.intValue().toString
  }

}

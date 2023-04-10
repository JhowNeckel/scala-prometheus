package metrics

import inj.AppModule
import io.prometheus.client.{Counter, Gauge, Histogram}

trait Metrics {
  this: AppModule =>

  final val requestLatency =
    Histogram.build()
      .name("request_latency_seconds")
      .help("Request latency in seconds")
      .register(registry)

  final val requests =
    Counter.build()
      .name("requests_total")
      .help("Total requests")
      .labelNames("status", "path", "method")
      .register(registry)

  final val inprogress =
    Gauge.build()
      .name("inprogress_requests")
      .help("In progress requests")
      .register(registry)

  final val errors =
    Counter.build()
      .name("errors_total")
      .help("Total errors")
      .register(registry)

}

package inj

import akka.actor.typed.ActorSystem
import io.prometheus.client.CollectorRegistry
import logging.CustomLogger
import metrics.Metrics
import org.slf4j.Logger

class AppModule(system: ActorSystem[_]) extends CustomLogger with Metrics {
  lazy val registry: CollectorRegistry = new CollectorRegistry()
  lazy val logger: Logger = system.log
}

object AppModule {
  def apply()(implicit system: ActorSystem[_]): AppModule = new AppModule(system)
}
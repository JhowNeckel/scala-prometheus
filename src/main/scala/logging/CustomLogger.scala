package logging

import inj.AppModule
import org.slf4j.event.Level
import spray.json.DefaultJsonProtocol._
import spray.json.{JsNull, JsString, JsValue, enrichAny}

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

trait CustomLogger {
  this: AppModule =>

  private case class LogMessage(source: String, event: String, pld: JsValue) {
    private val dateFormat: DateTimeFormatter =
      DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS")

    def toMap: Map[String, JsValue] = Map(
      "AT" -> LocalDateTime.now.format(dateFormat).toJson,
      "ARE" -> source.toJson,
      "ACT" -> event.toJson,
      "PLD" -> pld
    )
  }

  object log {

    private def log(level: Level, message: LogMessage): Unit =
      logger.makeLoggingEventBuilder(level).log(message.toMap.toJson.compactPrint)

    def info(source: AnyRef, event: String, pld: JsValue): Unit =
      log(Level.INFO, LogMessage(source.toString, event, pld))

    def info(source: AnyRef, event: String, pld: String): Unit =
      info(source.toString, event, JsString(pld))

    def info(source: AnyRef, event: String): Unit =
      info(source.toString, event, JsNull)

    def warn(source: AnyRef, event: String, pld: JsValue): Unit =
      log(Level.WARN, LogMessage(source.toString, event, pld))

    def warn(source: AnyRef, event: String, pld: String): Unit =
      warn(source.toString, event, JsString(pld))

    def warn(source: AnyRef, event: String): Unit =
      warn(source.toString, event, JsNull)

    def error(source: AnyRef, event: String, pld: JsValue): Unit =
      log(Level.ERROR, LogMessage(source.toString, event, pld))

    def error(source: AnyRef, event: String, pld: String): Unit =
      error(source, event, JsString(pld))

    def error(source: AnyRef, event: String): Unit =
      error(source, event, JsNull)

    def debug(source: AnyRef, event: String, pld: JsValue): Unit =
      log(Level.DEBUG, LogMessage(source.toString, event, pld))

    def debug(source: AnyRef, event: String, pld: String): Unit =
      debug(source, event, JsString(pld))

    def debug(source: AnyRef, event: String): Unit =
      debug(source, event, JsNull)
  }

}

import sbt._

object DependenciesDefinition {

  object Version {
    val akka = "2.8.0"
    val akkaHttp = "10.5.0"
    val prometheus = "0.16.0"
    val log4j = "2.20.0"
    val log4jApiScala = "12.0"
    val slf4j = "2.0.7"
    val jacksonDatabind = "2.14.2"
  }

  // Core
  val akkaTyped = "com.typesafe.akka" %% "akka-actor-typed" % Version.akka
  val akkaStream = "com.typesafe.akka" %% "akka-stream" % Version.akka
  val akkaHttp = "com.typesafe.akka" %% "akka-http" % Version.akkaHttp

  // JSON
  val sprayJson = "com.typesafe.akka" %% "akka-http-spray-json" % Version.akkaHttp

  // Metrics
  val simpleclient = "io.prometheus" % "simpleclient" % Version.prometheus
  val simpleclientHttpserver = "io.prometheus" % "simpleclient_httpserver" % Version.prometheus

  // Logging
  val slf4jApi = "org.slf4j" % "slf4j-api" % Version.slf4j
  val log4jCore = "org.apache.logging.log4j" % "log4j-core" % Version.log4j
  val log4jApiScala = "org.apache.logging.log4j" %% "log4j-api-scala" % Version.log4jApiScala
  val log4jSlf4j = "org.apache.logging.log4j" % "log4j-slf4j2-impl" % Version.log4j

}

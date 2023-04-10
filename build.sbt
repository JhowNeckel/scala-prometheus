import DependenciesDefinition._
import com.typesafe.sbt.packager.MappingsHelper.directory
import com.typesafe.sbt.packager.docker._

ThisBuild / version := "0.1.0-SNAPSHOT"
ThisBuild / scalaVersion := "2.13.10"

lazy val root = (project in file("."))
  .enablePlugins(JavaAppPackaging)
  .settings(scalacSettings)
  .settings(binSettings)
  .settings(dockerSettings)
  .settings(
    name := "scala-prometheus",
    libraryDependencies ++= Seq(
      akkaTyped,
      akkaStream,
      akkaHttp,
      sprayJson,
      simpleclient,
      simpleclientHttpserver,
      slf4jApi,
      log4jCore,
      log4jApiScala,
      log4jSlf4j
    )
  )

lazy val scalacSettings = Seq(
  packageDoc / publishArtifact := false,
  packageSrc / publishArtifact := false,
  scalacOptions ++= Seq("-encoding", "utf-8") // Specify character encoding used by source files
)

lazy val binSettings = Seq(
  Compile / mainClass := Some("main.AppBootstrap"),
  Universal / mappings ++= directory("project/ext/"),
  Universal / javaOptions ++= Seq("-Djava.net.preferIPv4Stack=true")
)

lazy val dockerSettings: Seq[Setting[_]] = {
  val installLocation = "/opt/scala-prometheus"

  Seq(
    dockerBaseImage := "docker.io/library/openjdk:17-alpine",
    dockerEnvVars ++= Map("APP_HOME" -> s"$installLocation/"),
    Docker / maintainer := "Jhonatan Neckel <neckel.carvalho@gmail.com>",
    Docker / packageName := name.value,
    Docker / version := version.value,
    Docker / defaultLinuxInstallLocation := installLocation,
    dockerCommands += Cmd("USER", "root"),
    dockerCommands += ExecCmd("RUN", "apk", "update"),
    dockerCommands += ExecCmd("RUN", "apk", "add", "bash"),
    dockerCommands += Cmd("USER", "demiourgos728")
  )
}

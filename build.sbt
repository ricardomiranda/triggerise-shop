// import Dependencies._

ThisBuild / scalaVersion     := "2.12.8"
ThisBuild / version          := "1.0.0"
ThisBuild / organization     := "com.ricardomiranda"
ThisBuild / organizationName := "Ricardo Miranda"

lazy val dependencies = Seq(
  // https://mvnrepository.com/artifact/org.scalatest/scalatest
  "org.scalatest" %% "scalatest" % "3.0.5" % Test,
  // https://mvnrepository.com/artifact/com.typesafe.scala-logging/scala-logging
  "com.typesafe.scala-logging" %% "scala-logging" % "3.9.2",
  // https://mvnrepository.com/artifact/com.github.scopt/scopt
  "com.github.scopt" %% "scopt" % "4.0.0-RC2",
  // https://mvnrepository.com/artifact/io.spray/spray-json
  "io.spray" %% "spray-json" % "1.3.5"
)

lazy val root = (project in file("."))
  .settings(
    mainClass in (Compile, packageBin) := Some("com.ricardomiranda.shop.Main"),
    name := "TriggeriseShop",
    libraryDependencies ++= dependencies
  )

// Simple and constant jar name
assemblyJarName in assembly := s"triggerise-shop.jar"

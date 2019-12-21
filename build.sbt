// import Dependencies._

ThisBuild / scalaVersion     := "2.12.8"
ThisBuild / version          := "0.1.0-SNAPSHOT"
ThisBuild / organization     := "com.ricardomiranda"
ThisBuild / organizationName := "Ricardo Miranda"

lazy val dependencies = Seq(
  // https://mvnrepository.com/artifact/org.scalatest/scalatest
  "org.scalatest" %% "scalatest" % "3.0.5" % Test
)

lazy val root = (project in file("."))
  .settings(
    mainClass in (Compile, packageBin) := Some("com.ricardomiranda.shop.Main"),
    name := "TriggeriseShop",
    libraryDependencies ++= dependencies
  )

// Simple and constant jar name
assemblyJarName in assembly := s"triggerise-shop.jar"

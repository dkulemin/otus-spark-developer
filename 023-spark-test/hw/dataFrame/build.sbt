ThisBuild / version := "0.1.0-SNAPSHOT"

ThisBuild / scalaVersion := "2.13.17"

lazy val root = (project in file("."))
  .settings(
    name := "dataFrame"
  )

val sparkVersion = "3.5.7"
lazy val log4jVersion = "2.22.1"

libraryDependencies ++= Seq(
  "org.apache.spark" %% "spark-core" % "3.5.7",
  "org.apache.spark" %% "spark-sql" % "3.5.7",
  "com.fasterxml.jackson.core" % "jackson-core" % "2.16.1",
  "org.scalatest" %% "scalatest" % "3.2.17" % "test",
  "com.github.mrpowers" %% "spark-fast-tests" % "1.3.0" % "test",
  "org.apache.logging.log4j" %% "log4j-api-scala" % "13.0.0",
  "org.apache.logging.log4j" % "log4j-api" % log4jVersion,
  "org.apache.logging.log4j" % "log4j-core" % log4jVersion
)

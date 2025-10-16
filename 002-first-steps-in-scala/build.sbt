ThisBuild / version := "0.1.0-SNAPSHOT"

ThisBuild / scalaVersion := "2.13.17"

lazy val root = (project in file("."))
  .settings(
    name := "002-first-steps-in-scala"
  )

libraryDependencies += "org.scalatest" %% "scalatest" % "3.2.19" % Test
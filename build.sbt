name := "make"

version := "0.0.1"

scalaVersion := "2.13.1"

scalacOptions ++= Seq(
  "-encoding", "utf8",
  "-Xfatal-warnings",
  "-deprecation",
  "-unchecked",
  "-feature",
  "-Xlint"
)

libraryDependencies += "org.scalatest" %% "scalatest" % "3.1.0" % "test"
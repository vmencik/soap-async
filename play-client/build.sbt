name := "soap-async"

version := "1.0-SNAPSHOT"

scalaVersion := "2.11.7"

lazy val root = (project in file(".")).enablePlugins(PlayScala)

libraryDependencies ++= Seq(
  "org.apache.cxf" % "cxf-rt-frontend-jaxws" % "2.7.4",
  "org.apache.cxf" % "cxf-rt-transports-http-hc" % "2.7.4"
)

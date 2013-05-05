import sbt._
import Keys._
import play.Project._

object ApplicationBuild extends Build {

  val appName         = "soap-async"
  val appVersion      = "1.0-SNAPSHOT"

  val appDependencies = Seq(
    "org.apache.cxf" % "cxf-rt-frontend-jaxws" % "2.7.4",
    "org.apache.cxf" % "cxf-rt-transports-http-hc" % "2.7.4"
  )


  val main = play.Project(appName, appVersion, appDependencies).settings(
    
  )

}

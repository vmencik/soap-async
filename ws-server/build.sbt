name := "ws-server"

version := "1.0-SNAPSHOT"

autoScalaLibrary := false

crossPaths := false

// The following is a workaround for http://bugs.java.com/view_bug.do?bug_id=8066077
// with Java >= 8u40, this is no longer necessary

fork in run := true

javaOptions in run += "-Djdk.logging.allowStackWalkSearch=true"

// because we fork the JVM we need to connect the input to it
connectInput in run := true
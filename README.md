Non-blocking SOAP client using Scala, Play and CXF
==================================================

ws-server
---------
An implementation of a trivial SOAP web service using JAX-WS and the built-in HTTP server in JRE.

The folder contains an [SBT](http://www.scala-sbt.org/) project.

	> sbt run

This will start the web service endpoint at [http://localhost:8080/ws-server/AddNumbers](http://localhost:8080/ws-server/AddNumbers).

play-client
------------------------------------------------------
A [Play](http://www.playframework.com/) application that consumes the SOAP web service.

It uses the [CXF](http://cxf.apache.org/docs/asynchronous-client-http-transport.html) to implement a non-blocking SOAP client.
Without CXF in classpath, the code will still work, but JAXWS will revert to the default implementation (JAXWS-RI - at least in Oracle Java) that uses blocking HTTP client. 

The client expects the web service to be running at: [http://localhost:8080/ws-server/AddNumbers](http://localhost:8080/ws-server/AddNumbers).

See [play-client/app/controllers/AsyncSoapController.scala](/play-client/app/controllers/AsyncSoapController.scala) for the details.

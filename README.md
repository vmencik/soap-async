Non-blocking SOAP client using Scala, Play and CXF
==================================================
You can read more about this problem in a blog post at [czechscala.com](http://czechscala.com/2013/05/13/non-blocking-soap-web-services-invocation/) (in English)
or at [dev.etnetera.cz](http://dev.etnetera.cz/cz/scala/neblokujici_volani_soap_webovych_sluzeb.html) (in Czech).

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

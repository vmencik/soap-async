package controllers
import java.util.concurrent.ExecutionException

import scala.concurrent.Future
import scala.concurrent.Promise
import scala.util.Try

import async.client.AddNumbersResponse
import async.client.AddNumbersService
import javax.xml.ws.AsyncHandler
import javax.xml.ws.{ Response => JaxwsResponse }
import play.api._
import play.api.libs.concurrent.Execution.Implicits._
import play.api.mvc._

object AsyncSoapController extends Controller {

  def add(a: Int, b: Int) = Action.async {
    addUsingWebService(a, b) map {
      case Right(sum) => Ok(s"And the sum is: $sum")
      case Left(errMsg) => InternalServerError(s"Ooops, $errMsg")
    }
  }

  private def addUsingWebService(a: Int, b: Int): Future[Either[String, Int]] = {
    // watch out: JAXWS service constructors access the (possibly remote) WSDL a create the service delegate
    // this will cause a blocking HTTP GET
    // the service instance could be shared by all requests
    //  - maybe created on application startup
    //	- the WSDL could be stored locally to prevent dependency on the web service's availability at startup time
    // thread-safety of the port depends on the JAXWS implementation
    val svc = new AddNumbersService
    val port = svc.getAddNumbersImplPort()

    JaxwsAsyncAdapter.invoke[AddNumbersResponse] { handler =>
      port.addNumbersAsync(a, b, handler)
    } map { resp =>
      Right(resp.getReturn)
    } recover {
      case e: ExecutionException =>
        Logger.error("Error invoking SOAP web service", e)
        Left(s"Error invoking SOAP web service: ${e.getCause.getMessage}")
    }
  }

}

object JaxwsAsyncAdapter {

  /**
   * Returns a Future containing a result of an asynchronous JAX-WS proxy method invocation.
   * @param invoker function that invokes a JAX-WS proxy method with the specified AsyncHandler
   * @return Future containing the eventual result of JAX-WS proxy method invocation
   */
  def invoke[A](invoker: AsyncHandler[A] => Unit): Future[A] = {
    val promise = Promise[A]()
    val handler = new AsyncHandler[A] {
      override def handleResponse(response: JaxwsResponse[A]) = {
        // javax.xml.ws.Response is a java.util.concurrent.Future implementation
        // that might throw an exception (like java.util.concurrent.ExecutionException)
        // when the computation result is retrieved (response.get)
        promise.complete(Try(response.get))
      }
    }
    invoker(handler)
    promise.future
  }

}
package async

import async.client.AddNumbersService
import javax.xml.ws.Response
import async.client.AddNumbersResponse
import javax.xml.ws.BindingProvider
import org.apache.cxf.transport.http.asyncclient.AsyncHTTPConduit
import scala.concurrent.Future
import javax.xml.ws.AsyncHandler
import scala.concurrent.Promise
import scala.concurrent.ExecutionContext
import scala.util.Success
import scala.util.Failure
import scala.util.Try

object Main extends App {

  val svc = new AddNumbersService
  val port = svc.getAddNumbersImplPort()

  //  val future = port.addNumbersAsync(5, 10, (resp: Response[AddNumbersResponse]) => {
  //    println(resp.get.getReturn)
  //    println(Thread.currentThread().getName())
  //  })
  //  println(future)

  import ExecutionContext.Implicits._
  val future = JaxwsAsync.invoke[AddNumbersResponse] { handler =>
    port.addNumbersAsync(5, 10, handler)
  }
  future.onFailure {
    case e => e.printStackTrace()
  }

  for (r <- future) {
    println(r.getReturn())
    println(Thread.currentThread().getName())
  }

  //  future onComplete {
  //    case Success(r) =>
  //      println(r.getReturn())
  //      println(Thread.currentThread().getName())
  //    case Failure(e) => throw e
  //  }
}

object JaxwsAsync {

  def invoke[A](invoker: AsyncHandler[A] => Unit): Future[A] = {
    val promise = Promise[A]()
    val handler = new AsyncHandler[A] {
      override def handleResponse(response: Response[A]) = promise.complete(Try(response.get))
    }
    invoker(handler)
    promise.future
  }

}


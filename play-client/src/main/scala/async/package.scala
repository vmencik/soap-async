import javax.xml.ws.AsyncHandler
import javax.xml.ws.Response

import scala.language.implicitConversions

package object async {

  implicit def fun2AsyncHandler[T](fun: Response[T] => Unit): AsyncHandler[T] =
    new AsyncHandler[T] {

      override def handleResponse(response: Response[T]) = fun(response)

    }

}
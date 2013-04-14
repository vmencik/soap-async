package controllers
import scala.concurrent.ExecutionContext
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

import play.api._
import play.api.mvc._

object TestController extends Controller {

  def test(u: String, p: String) = Action {
    AsyncResult {
      login(u, p) flatMap {
        case Left(e) => Future.successful(Ok(e))
        case Right(token) =>
          fetchSubscription(token) flatMap {
            case Left(e) => Future.successful(Ok(e))
            case Right(sub) =>
              val eventUser = fetchUserInfo(sub)
              val eventWl = fetchWishList(sub)
              for {
                user <- eventUser
                wl <- eventWl
              } yield {
                (user, wl) match {
                  case (Right(u), Right(w)) => Ok(s"User: ${u.name}, wishlist: ${w.items}")
                  case _ => Ok((user.left.toSeq ++ wl.left.toSeq).toString)
                }
              }
          }
      }

    }
  }

  def login(u: String, p: String)(implicit ec: ExecutionContext): Future[Either[String, Token]] = {
    Future {
      u match {
        case "Franta" => Right(Token("xyz"))
        case "Anton" => Right(Token("vbnm"))
        case "Lojza" => Right(Token("abcd"))
        case "Pepa" => Right(Token("0"))
        case _ => Left("auth.denied")
      }
    } recover {
      case e: Exception =>
        e.printStackTrace()
        Left("auth.error")
    }
  }

  def fetchSubscription(token: Token)(implicit ec: ExecutionContext): Future[Either[String, Subscription]] = {
    Future {
      token match {
        case Token("0") => Left("subscr.notFound")
        case Token(x) => Right(Subscription("subscr-" + x))
      }
    } recover {
      case e: Exception =>
        e.printStackTrace()
        Left("subscr.error")
    }
  }

  def fetchUserInfo(sub: Subscription)(implicit ec: ExecutionContext): Future[Either[String, UserInfo]] = {
    Future {
      sub match {
        case Subscription("subscr-abcd") => Left("userInfo.notFound")
        case Subscription(code) => Right(UserInfo("name-" + code))
      }
    } recover {
      case e: Exception =>
        e.printStackTrace()
        Left("userInfo.error")
    }
  }

  def fetchWishList(sub: Subscription)(implicit ec: ExecutionContext): Future[Either[String, WishList]] = {
    Future {
      sub match {
        case Subscription("subscr-vbnm") => Right(WishList(List(1, 2, 3)))
        case _ => Right(WishList(Nil))
      }
    } recover {
      case e: Exception =>
        e.printStackTrace()
        Left("wishList.error")
    }
  }

  case class Token(value: String)
  case class Subscription(code: String)
  case class UserInfo(name: String)
  case class WishList(items: List[Int])

}
package uk.co.oldstreetjournal.quickstart

import cats.effect._
import com.comcast.ip4s._
import io.circe.literal._
import org.http4s.circe._
import org.http4s.dsl.io._
import org.http4s.ember.server._
import org.http4s.{HttpRoutes, QueryParamDecoder, Request, Response}
import org.reactormonk.{CryptoBits, PrivateKey}
import org.http4s.headers.Cookie
import cats._
import cats.effect._
import cats.implicits._
import cats.data._

import scala.util.Random
import scala.io.Codec

object Main extends IOApp {

  case class Weight(weight: Double);

  case class User(id: String, name: String);

  implicit val weightQueryParamDecode: QueryParamDecoder[Weight] =
    QueryParamDecoder[Double].map(Weight)

  object User {
    def unapply(id: String): Option[User] = id match {
      case "1" => Some(User(id, "aiden"))
      case _ => None
    }
  }

  object WeightQueryParamMatcher extends QueryParamDecoderMatcher[Weight]("weight")

  val helloWorldService: Kleisli[IO, Request[IO], Response[IO]] = HttpRoutes.of[IO] {
    case req @ POST -> Root / "hello" => ???
    case GET -> Root / "hello" / name =>
      Ok(s"Hello, $name.")
    case GET -> Root / "record" / User(user) :? WeightQueryParamMatcher(weight) =>
      Ok(json"""{"id": ${user.id}, "name": ${user.name}, "weight": ${weight.weight}}""")

  }.orNotFound

  def run(args: List[String]): IO[ExitCode] =
    EmberServerBuilder
      .default[IO]
      .withHost(ipv4"0.0.0.0")
      .withPort(port"8080")
      .withHttpApp(helloWorldService)
      .build
      .use(_ => IO.never)
      .as(ExitCode.Success)
}
// idea:
// a form which logs a weight against a time and date.
// persistence
// authentication
// some tests

//future:
// extrcat text from an image
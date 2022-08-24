package uk.co.oldstreetjournal.quickstart

import cats.data._
import cats.effect._
import com.comcast.ip4s._
import io.circe.literal._
import io.circe.generic.auto._
import io.circe.syntax._
import org.http4s.circe._
import org.http4s.dsl.io._
import org.http4s.ember.server._
import org.http4s.server.middleware.Logger
import org.http4s.{EntityDecoder, HttpRoutes, QueryParamDecoder, Request, Response}


object Main extends IOApp {

  case class Weight(weight: Double);

  case class User(id: String, name: String);

  implicit val weightQueryParamDecode: QueryParamDecoder[Weight] =
    QueryParamDecoder[Double].map(Weight)

  implicit val weightJsonDecode: EntityDecoder[IO, Weight] = jsonOf[IO, Weight]

  object User {
    def unapply(id: String): Option[User] = id match {
      case "1" => Some(User(id, "aiden"))
      case _ => None
    }
  }

  object WeightQueryParamMatcher extends QueryParamDecoderMatcher[Weight]("weight")

  val helloWorldService: Kleisli[IO, Request[IO], Response[IO]] = HttpRoutes.of[IO] {
    case GET -> Root / "record" / User(user) :? WeightQueryParamMatcher(weight) =>
      Ok(json"""{"id": ${user.id}, "name": ${user.name}, "weight": ${weight.weight}}""")
    case req @ POST -> Root / "record" / User(_) => {
      throw new Exception("aha")
      throw new Exception("aha")
      for {
      weight <- req.as[Weight]
      _ = println(weight)
      res <- Ok(weight.asJson)
    } yield {
      res
    }}

  }.orNotFound

  def run(args: List[String]): IO[ExitCode] =
    EmberServerBuilder
      .default[IO]
      .withHost(ipv4"0.0.0.0")
      .withPort(port"8080")
      .withHttpApp(Logger.httpApp(true,true)(helloWorldService))
      .build
      .use(x => {
        println(x)
        IO.never
      }).
      as(ExitCode.Success) 

}
// idea:
// a form which logs a weight against a time and date.
// persistence
// authentication
// some tests

//future:
// extrcat text from an image

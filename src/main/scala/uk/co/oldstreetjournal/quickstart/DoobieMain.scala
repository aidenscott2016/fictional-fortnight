//package uk.co.oldstreetjournal.quickstart
//
//import cats.effect.{ExitCode, IO, IOApp}
////import cats._, cats.data._, cats.implicits._
//import doobie._
//import doobie.implicits._
//
//
//object DoobieMain {
//  case class Country(code: String, name: String, pop: Int, gnp: Option[Double])
//  val xa = Transactor.fromDriverManager[IO](
//    "org.postgresql.Driver",
//    "jdbc:postgresql://127.0.0.1:5432/world",
//    "user",
//    "password"
//  )
//
//  val program2 = sql"select * from ".query[Int].unique
//  val p3 = sql"select name from country"
//    .query[String] // Query0[String]
//    .stream
//    .take(5)
//    .compile.toList
//
//
//  val p4 = sql"select name, code, population, gnp from country"
//    .query[Country]
//    .stream
//    .take(5)
//    //.compile.toList
//
//  def saveCountry(name: String): IO[Int] = {
//    val c = new Country("gg", name, pop = 10, gnp = None)
//    val q = "insert into country(code, name, population, gnp) values (?, ?, ?, ?)"
//    Update[Country](q).run(c).transact(xa)
//
//  }
//
//  override def run(args: List[String]): IO[ ExitCode] = {
//    for {
//      res <- saveCountry("aiden")
//      _ = println(res)
//    } yield {
//     ExitCode.Success
//    }
//  }
//}

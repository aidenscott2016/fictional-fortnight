package uk.co.oldstreetjournal.quickstart.db

import cats.effect.{IO, Resource}
import doobie.hikari.HikariTransactor
import org.flywaydb.core.Flyway
import uk.co.oldstreetjournal.quickstart.config.Config.DatabaseConfig

import scala.concurrent.ExecutionContext

object Database {
  def transactor(config: DatabaseConfig, executionContext: ExecutionContext): Resource[IO, HikariTransactor[IO]] = {
    HikariTransactor.newHikariTransactor[IO](
      config.driver,
      config.url,
      config.user,
      config.password,
      executionContext
    )
  }

  def initialize(transactor: HikariTransactor[IO]): IO[Unit] = {
    transactor.configure { dataSource =>
      IO {
        val flyWay = Flyway.configure().dataSource(dataSource).load()
        flyWay.migrate()
        ()
      }
    }
  }
}

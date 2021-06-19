package com.aphiwe.testApi.core

import akka.actor.ActorSystem
import akka.stream.ActorMaterializer
import akka.stream.alpakka.cassandra.CassandraSessionSettings
import akka.stream.alpakka.cassandra.scaladsl.{CassandraSession, CassandraSessionRegistry}

object ImpExecutors {
  implicit val system = ActorSystem("app")
  implicit val executionContext = system.dispatcher
  implicit val materializer = ActorMaterializer()

  val sessionSettings = CassandraSessionSettings()
  implicit val cassandraSession: CassandraSession = CassandraSessionRegistry
    .get(system).sessionFor(sessionSettings)
}

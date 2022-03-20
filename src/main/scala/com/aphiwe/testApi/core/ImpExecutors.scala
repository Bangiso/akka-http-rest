package com.aphiwe.testApi.core

import akka.actor.ActorSystem
import akka.stream.ActorMaterializer

object ImpExecutors {
  implicit val system = ActorSystem("app")
  implicit val executionContext = system.dispatcher
  implicit val materializer = ActorMaterializer()
}

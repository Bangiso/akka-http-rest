package com.aphiwe.testApi


import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.stream.ActorMaterializer
import com.aphiwe.testApi.api.StudentRouts
import com.aphiwe.testApi.service.StudentSlice

import scala.io.StdIn

object Main extends StudentRouts with StudentSlice {
  implicit val system = ActorSystem("app")
  implicit val executionContext = system.dispatcher
  implicit val materializer = ActorMaterializer()

  def main(arg: Array[String]): Unit = {
    println(s"Starting online at http://localhost:8080...")
    Http().bindAndHandle(route, "localhost", 8080)
  }
}

package com.aphiwe.testApi


import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.stream.ActorMaterializer
import com.aphiwe.testApi.api.StudentRouts
import com.aphiwe.testApi.service.StudentSlice
import com.typesafe.config.ConfigFactory


object Main extends StudentRouts with StudentSlice {
  implicit val system = ActorSystem("app")
  implicit val executionContext = system.dispatcher
  implicit val materializer = ActorMaterializer()

  def main(arg: Array[String]): Unit = {
    val config =ConfigFactory.parseResourcesAnySyntax("application.conf").withFallback(ConfigFactory.load()).resolve()
    val port = config.getInt("http.port")
    val host = config.getString("http.host")
    println(s"Starting online at http://localhost:${port}...")
    Http().bindAndHandle(route, host, port)
  }
}

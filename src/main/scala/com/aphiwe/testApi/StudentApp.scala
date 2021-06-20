package com.aphiwe.testApi

import akka.http.scaladsl.Http
import com.aphiwe.testApi.api.StudentRouts
import com.aphiwe.testApi.core.ImpExecutors._
import com.aphiwe.testApi.service.StudentSlice
import com.typesafe.config.ConfigFactory

object StudentApp extends StudentRouts with StudentSlice {

  def main(arg: Array[String]): Unit = {
    val config =ConfigFactory.parseResourcesAnySyntax("application.conf").withFallback(ConfigFactory.load()).resolve()
    val port = config.getInt("http.port")
    val host = config.getString("http.host")
    println(s"Starting online at http://localhost:${port}...")
    Http().bindAndHandle(route, host, port)
  }
}

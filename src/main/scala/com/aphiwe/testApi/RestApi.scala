package com.aphiwe.testApi

import akka.http.scaladsl.Http
import com.aphiwe.testApi.api._
import com.aphiwe.testApi.service.StudentSlice
import scala.io.StdIn

object RestApi extends StudentSlice with  StudentRouts{

  def main(args: Array[String]) {

    val bindingFuture = Http().bindAndHandle(route, "localhost", 8080)
    println(s"Server online at http://localhost:8080/\nPress RETURN to stop...")
    StdIn.readLine()
    bindingFuture
      .flatMap(_.unbind())
      .onComplete(_ => system.terminate())

  }

}

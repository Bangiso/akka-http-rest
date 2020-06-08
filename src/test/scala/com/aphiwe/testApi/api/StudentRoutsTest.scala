package com.aphiwe.testApi.api

import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.testkit.Specs2RouteTest
import akka.http.scaladsl.server._
import akka.http.scaladsl.model.ContentTypes
import org.junit.runner.RunWith
import org.specs2.mutable.Specification
import org.specs2.runner.JUnitRunner
import spray.json._

@RunWith(classOf[JUnitRunner])
class StudentRoutsTest extends Specification with StudentRouts with Specs2RouteTest{

  "The service" should {
    "return a student for GET requests to the path /students/{id}" in {
      Get("/students/1") ~> Route.seal(route) ~> check {
        contentType  shouldEqual (ContentTypes.`application/json`)
        status === StatusCodes.OK
      }
    }
    "return a students list for GET requests to the path /students/all" in {
      Get("/students/all") ~> Route.seal(route) ~> check {
        contentType  shouldEqual (ContentTypes.`application/json`)
        status === StatusCodes.OK
      }
      }
    "return successfull \"student creation\" for POST requests to the path /student" in{
      Post("/student", """{"gpa":28.0,"id":1,"name":"Alex"}""".parseJson) ~> Route.seal(route) ~> check {
        status should ===(StatusCodes.OK)
        entityAs[String] shouldEqual "student created"
      }
    }
  }
}

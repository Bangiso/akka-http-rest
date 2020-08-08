package com.aphiwe.testApi.api

import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport._
import akka.http.scaladsl.model.{ContentTypes, StatusCodes}
import akka.http.scaladsl.server._
import akka.http.scaladsl.testkit.Specs2RouteTest
import com.aphiwe.testApi.service.StudentSlice
import org.junit.runner.RunWith
import org.specs2.mutable.Specification
import org.specs2.runner.JUnitRunner
import spray.json._


@RunWith(classOf[JUnitRunner])
class StudentRoutsTest extends Specification with Specs2RouteTest with StudentRouts with StudentSlice {

  "The service" should {
    "return a student for GET requests to the path /students/{id}" in {
      Get("/students/1") ~> route ~> check {
        contentType shouldEqual (ContentTypes.`application/json`)
        status === StatusCodes.OK
      }
    }

    "return a students list for GET requests to the path /students/all" in {
      Get("/students/all") ~> Route.seal(route) ~> check {
        contentType shouldEqual (ContentTypes.`application/json`)
        status === StatusCodes.OK
      }
    }

    "return successful student creation for POST requests to the path /student" in {
      Post("/student", """{"gpa":28.0,"id":1,"name":"Alex"}""".parseJson) ~> Route.seal(route) ~> check {
        status should ===(StatusCodes.OK)
        entityAs[String] shouldEqual "student created"
      }
    }

    "return successful student updated for PUT requests to the path /student/{i}" in {
      Put("/student/1", """{"gpa":28.0,"id":1,"name":"Alex"}""".parseJson) ~> Route.seal(route) ~> check {
        status should ===(StatusCodes.OK)
        entityAs[String] shouldEqual "student updated"
      }
    }
  }
}

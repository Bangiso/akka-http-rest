package com.aphiwe.testApi.api

import akka.Done
import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import com.aphiwe.testApi.RestApi.{fetchStudent, fetchStudents, saveStudent}
import com.aphiwe.testApi.core.StudentProtocol._

import scala.concurrent.Future

trait StudentRouts extends SprayJsonSupport{
  def route: Route =
    concat(
      get {
        path("student" / LongNumber) { studentId =>
          val maybeStud: Future[Option[Student]] = fetchStudent(studentId)
          onSuccess(maybeStud) {
            case Some(stud) => complete(stud)
            case None => complete(StatusCodes.NotFound)
          }
        }
      },
      get {
        path("student" / "all") {
          val maybeStud: Future[Option[List[Student]]] = fetchStudents()
          onSuccess(maybeStud) {
            case Some(stud) => complete(stud)
            case None => complete(StatusCodes.NotFound)
          }
        }
      },
      post {
        path("student") {
          entity(as[Student]) { student =>
            val saved: Future[Done] = saveStudent(student)
            onComplete(saved) { done =>
              complete("student created")
            }
          }
        }
      }
    )
}

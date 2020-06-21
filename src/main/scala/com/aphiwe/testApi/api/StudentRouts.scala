package com.aphiwe.testApi.api

import akka.Done
import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport._
import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import com.aphiwe.testApi.core.StudentProtocol._
import com.aphiwe.testApi.service.StudentSlice

import scala.concurrent.{ ExecutionContextExecutor, Future}

trait StudentRouts extends  StudentSlice {

  def route(implicit ec: ExecutionContextExecutor) : Route =
    concat(
      get {
        path("students" / LongNumber){ studentId =>
          val maybeStud: Future[Option[Student]] = studentService.fetchStudent(studentId)
          onSuccess(maybeStud) {
            case Some(stud) => complete(stud)
            case None => complete(StatusCodes.NotFound)
          }
        }
      },
      get {
        path("students" / "all") {
          val maybeStud: Future[Option[List[Student]]] = studentService.fetchStudents()
          onSuccess(maybeStud) {
            case Some(stud) => complete(stud)
            case None => complete(StatusCodes.NotFound)
          }
        }
      },
      post {
        path("student") {
          entity(as[Student]) { student =>
            val saved: Future[Done] = studentService.saveStudent(student)
            onComplete(saved) { done =>
              complete("student created")
            }
          }
        }
      }
    )
}

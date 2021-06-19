package com.aphiwe.testApi.api

import akka.Done
import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport._
import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.{ExceptionHandler, RejectionHandler, Route}
import ch.megard.akka.http.cors.scaladsl.settings.CorsSettings
import com.aphiwe.testApi.core.StudentProtocol._
import com.aphiwe.testApi.service.StudentSlice

import scala.concurrent.{ExecutionContextExecutor, Future}
import scala.util.{Failure, Success}

trait StudentRouts extends StudentSlice {

  def route(implicit ec: ExecutionContextExecutor): Route = {
    import ch.megard.akka.http.cors.scaladsl.CorsDirectives._
    val corsSettings = CorsSettings.defaultSettings

    val rejectionHandler = corsRejectionHandler withFallback RejectionHandler.default

    val exceptionHandler = ExceptionHandler {
      case e: NoSuchElementException => complete(StatusCodes.NotFound -> e.getMessage)
    }
    val handleErrors = handleRejections(rejectionHandler) & handleExceptions(exceptionHandler)

    handleErrors {
      cors(corsSettings) {
        handleErrors {
          concat(
            get {
              path("api" / "students" / IntNumber) { studentId =>
                onSuccess(studentService.fetchStudent(studentId)) {
                  case Some(stud) => complete(stud)
                  case _ => complete(StatusCodes.NotFound)
                }
              }
            },
            get {
              path("api" / "students") {
                onSuccess(studentService.fetchStudents()) {
                  case stud: Seq[Student] => complete(stud)
                  case _ => complete(StatusCodes.NotFound)
                }
              }
            },
            post {
              path("api" / "students") {
                entity(as[Student]) { student =>
                  handleUpdate(studentService.saveStudent(student), "student created")
                }
              }
            },
            put{
              path("api" / "students") {
                entity(as[Student]) { student =>
                  handleUpdate(studentService.updateStudent(student), "student updated")
                }
              }
            }
          )
        }
      }
    }
  }

  def handleUpdate(f: Future[Done], ms: String) = onComplete(f){ ff =>
    ff match {
      case Success(_) => complete(ms)
      case Failure(_) => complete(StatusCodes.InternalServerError)
    }
  }
}
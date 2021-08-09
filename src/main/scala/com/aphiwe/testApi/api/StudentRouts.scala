package com.aphiwe.testApi.api

import akka.Done
import akka.event.slf4j.Logger
import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport._
import akka.http.scaladsl.model.{ContentTypes, HttpEntity, StatusCodes}
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import com.aphiwe.testApi.core.StudentProtocol._
import com.aphiwe.testApi.service.StudentSlice

import scala.concurrent.{ExecutionContextExecutor, Future}
import scala.util.{Failure, Success}

trait StudentRouts extends StudentSlice {
  val logger = Logger("application")
  def route(implicit ec: ExecutionContextExecutor): Route = concat(
      get {
        path("index.html"){
          complete(HttpEntity(ContentTypes.`text/html(UTF-8)`,
           """<title>Index page</title>
             |<h1>Rest api </h1>
             |<h4>CRUD operations on</h4>
             |<ul>
             |    <li>GET &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;api/students/{id}</li>
             |    <li>GET &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;api/students</li>
             |    <li>POST &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;api/students</li>
             |    <li>PUT &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;api/students</li>
             |    <li>DELETE &nbsp;&nbsp;api/students</li>
             |</ul>
             |""".stripMargin))}
      },
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
            case stud: Seq[Student] =>
              if(stud.isEmpty){
                complete(StatusCodes.NotFound)
              } else complete(stud)
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
      },
      delete{
        path("api" / "students" / IntNumber) { studentId =>
            handleUpdate(studentService.removeStudent(studentId), "student removed")
        }
      }
    )

  def handleUpdate(f: Future[Done], ms: String) = onComplete(f){ ff =>
    ff match {
      case Success(_) => complete(ms)
      case Failure(ex) => logger.error(ex.getMessage)
        complete(StatusCodes.InternalServerError)
    }
  }
}
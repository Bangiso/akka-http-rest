package com.aphiwe.testApi.service

import akka.Done
import akka.actor.ActorSystem
import akka.stream.ActorMaterializer
import com.aphiwe.testApi.core.StudentProtocol._

import scala.concurrent.Future

trait StudentService {

  def fetchStudent(studentId: Long): Future[Option[Student]]

  def fetchStudents(): Future[Option[List[Student]]]

  def saveStudent(student: Student): Future[Done]

}

class StudentSlice extends StudentService {


  implicit val system = ActorSystem()
  implicit val materializer = ActorMaterializer()
  implicit val executionContext = system.dispatcher
  var students: List[Student] = List(
    new Student(1, "Aphiwe", 28),
    new Student(2, "San", 89)
  )

  override def fetchStudent(studentId: Long): Future[Option[Student]] = Future {
    students.find(st => st.id == studentId)
  }

  override def fetchStudents(): Future[Option[List[Student]]] = Future {
    Option(students)
  }


  override def saveStudent(student: Student): Future[Done] = {
    if (students.contains(student)) students
    else students=student::students
    Future{Done}
  }
}

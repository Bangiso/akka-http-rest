package com.aphiwe.testApi.dao

import akka.Done
import akka.actor.ActorSystem
import akka.stream.alpakka.slick.scaladsl._
import akka.stream.scaladsl._

import scala.concurrent.Future
import com.aphiwe.testApi.core.StudentProtocol._
import com.aphiwe.testApi.core.ImpExecutors._

object StudentDAO{

  implicit val session = SlickSession.forConfig("slick-mysql")
  system.registerOnTermination(session.close())
  import session.profile.api._

  def fetchStudents()(implicit system: ActorSystem): Future[Seq[Student]] = {
    val students =
      Slick
        .source(sql"SELECT id, name, gpa from students".as[Student])
        .runWith(Sink.seq)
    students
  }

  def fetchStudent(id:Int)(implicit system: ActorSystem): Future[Option[Student]] = {
    val student =
      Slick
        .source(sql"SELECT id, name, gpa from students where id = $id".as[Student])
        .runWith(Sink.headOption)
    student
  }

  def writeStud(student: Student)(implicit system: ActorSystem): Future[Done] = {
    Source(List(student))
      .runWith(
        Slick.sink(student => sqlu"INSERT INTO students(id, name, gpa) VALUES (${student.id}, ${student.name}, ${student.gpa})")
      )
  }

  def updateStudent(student: Student)(implicit system: ActorSystem ): Future[Done] = {
    Source(List(student))
      .runWith(
        Slick.sink(student => sqlu"UPDATE students SET name = ${student.name}, gpa = ${student.gpa} WHERE id = ${student.id}")
      )
  }
  def deleteStudentById(id: Int)(implicit system: ActorSystem): Future[Done] = {

    Source(List(id))
      .runWith(
        Slick.sink(id => sqlu"DELETE FROM students where id = $id")
      )
  }
}

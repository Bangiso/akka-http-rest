package com.aphiwe.testApi.service

import akka.Done
import com.aphiwe.testApi.core.StudentProtocol._
import com.aphiwe.testApi.core.ImpExecutors._
import com.aphiwe.testApi.dao.StudentDAO

import scala.concurrent.{ Future}

trait StudentService {

  def fetchStudent(studentId: Int): Future[Option[Student]]

  def fetchStudents(): Future[Seq[Student]]

  def saveStudent(student: Student): Future[Done]

  def updateStudent( student: Student): Future[Done]
}

trait StudentSlice{

  val studentService: StudentService= new StudentService {

    override def fetchStudent(studentId: Int): Future[Option[Student]] =  StudentDAO
      .fetchStudent(studentId)

    override def fetchStudents(): Future[Seq[Student]] = StudentDAO.
      fetchStudents()

    override def saveStudent(student: Student): Future[Done] = StudentDAO
      .writeStud(student)

    override def updateStudent(student: Student): Future[Done] = StudentDAO
      .updateStudent(student)
  }
}

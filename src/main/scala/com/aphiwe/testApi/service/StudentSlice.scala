package com.aphiwe.testApi.service

import akka.Done
import com.aphiwe.testApi.core.StudentProtocol._

import scala.collection.convert.ImplicitConversions.`collection asJava`
import scala.concurrent.{ExecutionContext, Future}

trait StudentService {

  
  def fetchStudent(studentId: Long)(implicit ex: ExecutionContext): Future[Option[Student]]

  def fetchStudents()(implicit ex: ExecutionContext): Future[Option[List[Student]]]

  def saveStudent(student: Student)(implicit ex: ExecutionContext): Future[Done]

  def updateStudent(id: Long, student: Student)(implicit ex: ExecutionContext): Future[Done]

}

trait StudentSlice {

  val studentService: StudentService = new StudentService {
    var students: List[Student] = List(
      new Student(1, "Aphiwe", 28),
      new Student(2, "San", 89)
    )

    override def fetchStudent(studentId: Long)(implicit ex: ExecutionContext): Future[Option[Student]] = Future {
      students.find(st => st.id == studentId)
    }

    override def fetchStudents()(implicit ex: ExecutionContext): Future[Option[List[Student]]] = Future {
      Option(students)
    }


    override def saveStudent(student: Student)(implicit ex: ExecutionContext): Future[Done] = {
      if (students.contains(student)) students
      else students = student :: students
      Future {
        Done
      }
    }

    override def updateStudent(id: Long, student: Student)(implicit ex: ExecutionContext): Future[Done] = {
      id match {
        case student.id => {
          val oldSt = students.find(std => std.id == id)
          students = student :: students
          students.remove(oldSt)
        }
        case _ => students = students
      }
      Future {
        Done
      }
    }
  }
}

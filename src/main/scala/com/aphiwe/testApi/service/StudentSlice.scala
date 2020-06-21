package com.aphiwe.testApi.service

import akka.Done
import com.aphiwe.testApi.core.StudentProtocol._

import scala.concurrent.{ExecutionContextExecutor, Future}

trait StudentService {

  def fetchStudent(studentId: Long)(implicit ex:ExecutionContextExecutor): Future[Option[Student]]

  def fetchStudents()(implicit ex:ExecutionContextExecutor): Future[Option[List[Student]]]

  def saveStudent(student: Student)(implicit ex:ExecutionContextExecutor): Future[Done]

}

trait StudentSlice{

  val studentService: StudentService=new StudentService {
    var students: List[Student] = List(
      new Student(1, "Aphiwe", 28),
      new Student(2, "San", 89)
    )

    override def fetchStudent(studentId: Long)(implicit ex:ExecutionContextExecutor): Future[Option[Student]] = Future {
      students.find(st => st.id == studentId)
    }

    override def fetchStudents()(implicit ex:ExecutionContextExecutor): Future[Option[List[Student]]] = Future {
      Option(students)
    }


    override def saveStudent(student: Student)(implicit ex:ExecutionContextExecutor): Future[Done] = {
      if (students.contains(student)) students
      else students = student :: students
      Future {
        Done
      }
    }
  }
}

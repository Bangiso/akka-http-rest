package com.aphiwe.testApi.dao
import akka.{Done}
import akka.actor.ActorSystem
import akka.stream.alpakka.cassandra.scaladsl.{CassandraSession, CassandraSource}
import akka.stream.scaladsl.{Sink, Source}
import com.aphiwe.testApi.core.StudentProtocol.Student
import akka.stream.alpakka.cassandra.CassandraWriteSettings
import akka.stream.alpakka.cassandra.scaladsl.CassandraFlow
import com.datastax.oss.driver.api.core.cql.{BoundStatement, PreparedStatement}

import scala.collection.immutable
import scala.concurrent.Future

object StudentDAO {

  def fetchStudents()(implicit system: ActorSystem, cassandraSession: CassandraSession): Future[Seq[Student]] = {

      CassandraSource(s"SELECT * FROM  students.students")
        .map(row =>
          Student( id = row.getInt("id"),
            name = row.getString("name"),
            gpa = row.getFloat("gpa").toDouble
          )
          ).runWith(Sink.seq)
  }
  def fetchStudent(id:Int)(implicit system: ActorSystem, cassandraSession: CassandraSession ): Future[Option[Student]] = {
      CassandraSource(s"SELECT * FROM  students.students where id = $id")
        .map(row =>
          Student( id = row.getInt("id"),
              name = row.getString("name"),
              gpa = row.getFloat("gpa").toDouble
          )
        ).runWith(Sink.headOption)
  }

  def writeStud(student: Student)(implicit system: ActorSystem, cassandraSession: CassandraSession ): Future[Done] = {

    val students = Source(immutable.Seq(student))
    val statementBinder: (Student, PreparedStatement) => BoundStatement =
      (stud, preparedStatement) => preparedStatement.bind(Int.box(stud.id), stud.name, stud.gpa.toFloat)


    students
      .via(
        CassandraFlow.create(
          CassandraWriteSettings.defaults,
          s"INSERT INTO students.students(id, name, gpa) VALUES (?, ?, ?)",
          statementBinder
        )
      ).runWith(Sink.ignore)
  }

  def updateStudent(student: Student)(implicit system: ActorSystem, cassandraSession: CassandraSession ): Future[Done] = {

    val students = Source(immutable.Seq(student))
    val statementBinder: (Student, PreparedStatement) => BoundStatement =
      (stud, preparedStatement) => preparedStatement.bind(stud.name, stud.gpa.toFloat, Int.box(stud.id))

    students
      .via(
        CassandraFlow.create(
          CassandraWriteSettings.defaults,
          s"UPDATE students.students SET name = ?, gpa = ? WHERE id = ?",
          statementBinder
        )
      ).runWith(Sink.ignore)
  }

    def deleteStudentById(id: Int)(implicit system: ActorSystem, cassandraSession: CassandraSession ): Future[Done] = {

      val statementBinder: (Int, PreparedStatement) => BoundStatement =
        (id, preparedStatement) => preparedStatement.bind(Int.box(id))

      Source(immutable.Seq(id))
        .via(
          CassandraFlow.create(
            CassandraWriteSettings.defaults,
            s"DELETE FROM students.students where id = ?",
            statementBinder
          )
        ).runWith(Sink.ignore)
  }
}

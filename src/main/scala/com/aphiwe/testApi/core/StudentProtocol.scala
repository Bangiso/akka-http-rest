package com.aphiwe.testApi.core

import slick.jdbc.GetResult
import spray.json.DefaultJsonProtocol

object StudentProtocol extends DefaultJsonProtocol {

  final case class Student(id: Int, name: String, gpa: Double)

  implicit val studentFmt = jsonFormat3(Student)

  implicit val getUserResult = GetResult(r => Student(r.nextInt, r.nextString, r.nextDouble()))

}
package com.aphiwe.testApi.core

import spray.json.DefaultJsonProtocol


object StudentProtocol extends DefaultJsonProtocol {

  final case class Student(id: Long, name: String, gpa: Double)

  final case class Students(students: List[Student])

  implicit val studentFmt = jsonFormat3(Student)

  implicit val studentsFmt = jsonFormat1(Students)
}
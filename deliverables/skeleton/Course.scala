//
//  Course
//  evaluator
//
//  Created by Yves VIAUD on 11/11/2017.
//

import scala.collection.mutable.ArrayBuffer

// We don't know yet how our teammates will model their classes
// So I'm using traits to synthesize what I'll need at least

trait IdentifiableObject {
  val identifier: String
}

trait User extends IdentifiableObject
trait Teacher extends User

trait Survey extends IdentifiableObject

// Class

class Course(val name: String, val owner: Teacher) {
  val identifier = "[Some generated identifier]" // using a helper tool

  val surveys = ArrayBuffer.empty[Survey]

  override def toString() : String = {
    return "Course " + identifier + " with " + surveys.length + " surveys"
  }
}

object Main {
  def main(args: Array[String]) : Unit = {
    val aTeacher = new Teacher { val identifier = "T21510111" }

    val aSurvey = new Survey { val identifier = "Q101" }
    val anotherSurvey = new Survey { val identifier = "Q201" }

    val aCourse = new Course("POOCAv", aTeacher)

    aCourse.surveys.append(aSurvey)
    aCourse.surveys.append(anotherSurvey)

    println(aCourse)
  }
}
package controllers

import javax.inject._
import play.api.mvc._
import play.api.libs.json._
import play.api.libs.json.Reads._
import play.api.libs.functional.syntax._

// Course example class to test

case class Course(name: String, id: Int)

object CourseGenerator {
  var list: List[Course] = {
    List(
      Course(
        "Cours de POOCAv",
        5
      ),
      Course(
        "Cours de ProgSync",
        42
      )
    )
  }

  def save(course: Course): Unit = {
    list = list ::: List(course)
  }
}

@Singleton
class ApiController @Inject()(cc: ControllerComponents) extends AbstractController(cc) {

  implicit val courseWrites: Writes[Course] = (
    (JsPath \ "name").write[String] and
      (JsPath \ "id").write[Int]
    )(unlift(Course.unapply))

  implicit val courseReads: Reads[Course] = (
    (JsPath \ "name").read[String] and
      (JsPath \ "id").read[Int](min(10))
    )(Course.apply _)

  def listCourses = Action {
    val json = Json.toJson(CourseGenerator.list)
    Ok(json)
  }

  def saveCourse = Action(parse.json) { request =>
    val courseResult = request.body.validate[Course]
    courseResult.fold(
      errors => {
        BadRequest(Json.obj("status" ->"KO", "message" -> JsError.toJson(errors)))
      },
      course => {
        CourseGenerator.save(course)
        Ok(Json.obj("status" ->"OK", "message" -> ("Course '"+course.name+"' saved.") ))
      }
    )
  }

}

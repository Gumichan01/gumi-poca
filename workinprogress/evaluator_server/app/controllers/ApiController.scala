package controllers

import javax.inject._
import play.api.data._
import play.api.data.Forms._
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

  val userForm = Form(
    tuple(
      "username" -> text,
      "password" -> text
    )
  )

  // Authentification

  def login = Action { implicit request =>
    val (username, password) = userForm.bindFromRequest.get
    println("Trying to login with " + username + ":" + password)
    Ok("Hello " + username + ", you're trying to login with: " + password)
    // Idéalement vérifier si les logins sont bons et rediriger avec
    // Redirect("path/location")
  }

  // Courses

  def listCourses = Action {
    val json = Json.toJson(CourseGenerator.list)
    Ok(json)
  }

  def show(cid: Long) = Action {
    Ok("Requested course with Id " + cid)
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

  // Quizz/Questionnaires

  def listQuizz(cid: Long) = Action {
    Ok("Request all quizz with course Id " + cid)
  }

}

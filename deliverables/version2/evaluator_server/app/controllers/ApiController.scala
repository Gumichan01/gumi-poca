package controllers

import javax.inject._

import play.api.data._
import play.api.data.Forms._
import play.api.mvc._
import play.api.libs.json._
import play.api.libs.json.Reads._
import play.api.libs.functional.syntax._

import scala.collection.mutable.ListBuffer

import model._


// Course example class to test

/*
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
*/

@Singleton
class ApiController @Inject()(cc: ControllerComponents) extends AbstractController(cc) {

  val serverInstance = Server

  /*
  implicit val courseWrites: Writes[Course] = (
    (JsPath \ "name").write[String] and
      (JsPath \ "id").write[Int]
    )(unlift(model.Course.unapply))

  implicit val courseReads: Reads[Course] = (
    (JsPath \ "name").read[String] and
      (JsPath \ "id").read[Int](min(10))
    )(Course.apply _)
  */

  val loginForm = Form(
    tuple(
      "surname" -> text,
      "password" -> text
    )
  )

  val registrationForm = Form(
    tuple(
      "surname" -> text,
      "name" -> text,
      "password" -> text,
      "role" -> text
    )
  )

  val courseForm = Form(
    tuple(
      "profId" -> number,
      "name" -> text,
      "content" -> text
    )
  )

  // Authentification & Registration

  def login = Action { implicit request =>
    val (surname, password) = loginForm.bindFromRequest.get
    val result = serverInstance.userLogIn(surname, password)
    result match {
      case true => Redirect("/").withSession("connected" -> surname)
      case false => Redirect("/connexion.html").flashing("loginFailure" -> "true")
    }
    // A voir si on redirige
    // Redirect("path/location")
  }

  def registration = Action { implicit request =>
    val (surname, name, password, role) = registrationForm.bindFromRequest.get
    val result = serverInstance.addUser(surname, name, password, role)
    result match {
      case true => Redirect("/").withSession("connected" -> surname).flashing("registrationSuccess" -> "true")
      case false => Redirect("/inscription.html").flashing("registrationFailure" -> "true")
    }
  }

  def disconnect = Action { implicit request =>
    Redirect(routes.HomeController.index()).withNewSession.flashing("disconnect" -> "true")
  }

  // Users

  def listUsers = Action {
    Ok("List Users here !")
  }

  def detailsAboutMe = Action { implicit request =>
    request.session.get("connected").map { user =>
      Ok("You're " + user)
    }.getOrElse {
      Unauthorized("Accès non autorisé. Veuillez vous connecter !")
    }
  }

  // Courses

  def listCourses = Action {
    //val json = Json.toJson(CourseGenerator.list)
    Ok("empty for now")
  }

  def show(cid: Long) = Action {
    Ok("Requested course with Id " + cid)
  }

  def newCourse = Action { implicit request =>
    val (profId, name, content) = courseForm.bindFromRequest.get
    val result = serverInstance.addCourse(name, content, profId)
    result._1 match {
      case true => Redirect("/cours/" + result._2 + ".html").flashing("courseSuccess" -> "true")
      case false => Redirect("/newcours.html").flashing("courseFailure" -> "true")
    }
  }

  def suscribe(cid: Long) = Action { implicit request =>
    request.session.get("connected").map { user =>
      (serverInstance.studentSuscribes(cid, user)) match {
        case true => Redirect("/cours/" + cid + ".html").flashing("susbcribeSuccess" -> "true")
        case false => Redirect("/cours/" + cid + ".html").flashing("suscribeFailure" -> "true")
      }
    }.getOrElse {
      Ok(views.html.inscription("Inscription", false, null))
    }
  }

  // Pour parser du JSON
  /*
  def saveCourse = Action(parse.json) { implicit request =>
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
  */

  // Quizz/Questionnaires

  def newSurvey = Action { implicit request =>
    (request.body.asFormUrlEncoded) match {
      case Some(bodyFormatted) => interpretSurveyBody(bodyFormatted)
      case None => BadRequest("Bad request")
    }
  }

  def interpretSurveyBody(body: Map[String, Seq[String]]) : Result = {
    var courseIdOpt = body.get("courseId")
    var questionsCount = 1
    var continueInterpreting = true
    var allQuestions: List[Question] = Nil
    var allQcmQuestions: List[MultipleChoiceQuestion] = Nil
    var survey: Survey = MCSurvey(Nil)

    // var allSourceCodeQuestions: List[]

    if (courseIdOpt == None)
      BadRequest("No Course ID")

    val courseId = courseIdOpt.get.head

    while (continueInterpreting) {
      var questionType = body.get(questionsCount + "questionType")

      if (questionType == None)
        continueInterpreting = false

      (questionType.get.head) match {
        case "qcm" => {
          val possibilitiesCount = body.get(questionsCount + "qcmPossibilities").get.head.toInt
          val intitule = body.get(questionsCount + "questionQuestion").get.head
          var answers: List[Answer] = Nil
          var goodAnswers: List[Answer] = Nil

          for (i <- 0 to possibilitiesCount) {
            val currentPossibility = body.get(questionsCount + "qcmString" + (i+1)).get.head
            answers = (new TextAnswer(currentPossibility)) :: answers
          }

          val goodAnswersFromForm = body.get(questionsCount + "goodAnswers").get.head.split('|')

          for (ga <- goodAnswersFromForm) {
            goodAnswers = (answers(ga.toInt - 1)) :: goodAnswers
          }

          val qcmQuestion = new MultipleChoiceQuestion(intitule, answers, goodAnswers)
          allQcmQuestions = qcmQuestion :: Nil

          survey = MCSurvey(allQcmQuestions)
        }
        case "code" => {
          Ok("ok")
        }
      }
    }

    Ok("ok")
  }

  def listSurveys(cid: Long) = Action {
    Ok("Request all quizz with course Id " + cid)
  }
}

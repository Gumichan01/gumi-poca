package controllers

import javax.inject._

import play.api.data._
import play.api.data.Forms._
import play.api.mvc.{request, _}
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
    request.session.get("connected").map { user =>
      Server.userWithSurname(user) match {
        case Some(u) => {
          u match {
            case p: Professor => {
              (request.body.asFormUrlEncoded) match {
                case Some(bodyFormatted) => interpretSurveyBody(p, bodyFormatted)
                case None => BadRequest("Bad request")
              }
            }
            case s: Student => Unauthorized("Accès réservé aux professeurs !")
          }
        }
        case None => Unauthorized("Accès non autorisé. Veuillez vous connecter !")
      }
    }.getOrElse {
      Unauthorized("Accès non autorisé. Veuillez vous connecter !")
    }
  }

  def submitAnswerSheet = Action { implicit request =>
    val fBody = request.body.asFormUrlEncoded.get
    val courseIdOpt = fBody.get("courseId")
    val surveyIdOpt = fBody.get("surveyId")

    if (courseIdOpt == None || surveyIdOpt == None) {
      BadRequest("Pas d'identifiants trouvés...")
    } else {
      val courseId = courseIdOpt.get.head
      val surveyId = surveyIdOpt.get.head
      val surveyOpt = Server.getSurvey(courseId.toLong, surveyId.toLong)
      val surveyType = fBody.get("surveyType").get.head

      surveyType match {
        case "qcm" => {
          if (surveyOpt != None) {
            val survey = surveyOpt.get.asInstanceOf[MCSurvey]
            val numberOfQuestions = surveyOpt.get.numberOfQuestions
            val sheet = new AnswerSheet(surveyId.toInt)
            for (q <- survey.questionl) {
              val currentAnswerOpt = fBody.get("answer" + q.id)
              if (currentAnswerOpt == None) {
                val t = TextAnswer("nullresponse")
                sheet.addAnswer(t, q.id)
              }
              else {
                val currentAnswer = currentAnswerOpt.get
                if (currentAnswer.size == 1) {
                  val t = TextAnswer(currentAnswer.head)
                  sheet.addAnswer(t, q.id)
                } else {
                  for (ua <- currentAnswer) {
                    val t = TextAnswer(ua)
                    sheet.addAnswer(t, q.id)
                  }
                }
              }
            }

            val result = Server.evaluateAnswerSheet(survey, sheet)
            val retFlash = "Votre score : " + result + " / " + numberOfQuestions

            Redirect("/cours/"+courseId+"/surveys/"+surveyId+".html").flashing("result" -> retFlash)
          }
          else {
            BadRequest("Aucun Questionnaire avec ces identifiants")
          }
        }
        case "code" => {
          if (surveyOpt != None) {
            val survey = surveyOpt.get.asInstanceOf[CodeSurvey]
            val numberOfQuestions = surveyOpt.get.numberOfQuestions
            val sheet = new AnswerSheet(surveyId.toInt)

            for (q <- survey.questionl) {
              val currentAnswerOpt = fBody.get("answer" + q.id)
              if (currentAnswerOpt == None) {
                val t = CodeAnswer("nullresponse")
                sheet.addAnswer(t, q.id)
              }
              else {
                val currentAnswer = currentAnswerOpt.get.head
                val ca = CodeAnswer(currentAnswer)
                sheet.addAnswer(ca, q.id)
              }
            }

            val result = Server.evaluateAnswerSheet(survey, sheet)
            val numberOfExpectations = survey.questionl.foldLeft(0) { _ + _.ganswer.size }
            val retFlash = "Votre score : " + result + " / " + numberOfExpectations

            Redirect("/cours/"+courseId+"/surveys/"+surveyId+".html").flashing("result" -> retFlash)
          }
          else {
            BadRequest("Aucun Questionnaire avec ces identifiants")
          }
        }
      }
    }
  }

  def interpretSurveyBody(p: Professor, body: Map[String, Seq[String]]) : Result = {
    var courseIdOpt = body.get("courseId")
    var questionsCount = 1
    var continueInterpreting = true
    var surveyType = "unknown"
    var allQcmQuestions: List[MultipleChoiceQuestion] = Nil
    var allCodeQuestions: List[SourceCodeQuestion] = Nil

    // var allSourceCodeQuestions: List[]

    if (courseIdOpt == None)
      BadRequest("No Course ID")

    val courseId = courseIdOpt.get.head

    while (continueInterpreting) {
      var questionType = body.get(questionsCount + "questionType")

      if (questionType == None) {
        continueInterpreting = false
      } else {
        (questionType.get.head) match {
          case "qcm" => {
            surveyType = "qcm"
            val possibilitiesCount = body.get(questionsCount + "qcmPossibilities").get.head.toInt
            val intitule = body.get(questionsCount + "questionQuestion").get.head
            var answers: List[Answer] = Nil
            var goodAnswers: List[Answer] = Nil

            for (i <- 1 to possibilitiesCount) {
              val currentPossibility = body.get(questionsCount + "qcmString" + (i)).get.head
              answers = answers :+ (new TextAnswer(currentPossibility))
            }

            val goodAnswersFromForm = body.get(questionsCount + "goodAnswers").get.head.split('|')

            for (ga <- goodAnswersFromForm) {
              goodAnswers = (answers(ga.toInt - 1)) :: goodAnswers
            }

            val qcmQuestion = new MultipleChoiceQuestion(intitule, answers, goodAnswers)
            allQcmQuestions = allQcmQuestions :+ qcmQuestion

            questionsCount += 1
          }
          case "code" => {
            surveyType = "code"
            val functionName = body.get(questionsCount + "functionName").get.head
            val argumentsCount = body.get(questionsCount + "argumentsCount").get.head.toInt
            val argumentsType = body.get(questionsCount + "argumentsType").get.head.split(",")
            val funcRetType = body.get(questionsCount + "funcReturnType").get.head
            val expectationsCount = body.get(questionsCount + "expectationsCount").get.head.toInt
            val intitule = body.get(questionsCount + "questionQuestion").get.head

            val expectations = body.get(questionsCount + "expectations").get.head.split(",")
            val forArgs = body.get(questionsCount + "forArgs").get.head.split(",")

            var answers: List[(List[(String, String)], String)] = Nil

            for (i <- 1 to expectationsCount) {
              var args: List[(String, String)] = Nil
              val expect = expectations(i-1)
              for (i <- 1 to argumentsCount) {
                val argType = argumentsType(i-1)
                val argValue = forArgs(i-1)
                args = (argType, argValue) :: args
              }
              answers = (args,expect)::answers
            }

            val scq = new SourceCodeQuestion(intitule, functionName, answers)
            allCodeQuestions = allCodeQuestions :+ scq

            questionsCount += 1
          }
        }
      }
    }

    surveyType match {
      case "qcm" => {
        val survey = MCSurvey(allQcmQuestions)
        Server.addSurvey(survey)

        val course = Server.getCourse(courseId.toLong)

        course match {
          case Some(c) => p.addQuestionnaire(survey, c)
          case None => BadRequest("Pas de cours associé")
        }

        Redirect("/cours/" + courseId + ".html").flashing("surveyRegistrationSuccess" -> "true")
      }
      case "code" => {
        val survey = CodeSurvey(allCodeQuestions)
        Server.addSurvey(survey)

        val course = Server.getCourse(courseId.toLong)

        course match {
          case Some(c) => p.addQuestionnaire(survey, c)
          case None => BadRequest("Pas de cours associé")
        }

        Redirect("/cours/" + courseId + ".html").flashing("surveyRegistrationSuccess" -> "true")
      }
    }
  }

  def listSurveys(cid: Long) = Action {
    Ok("Request all quizz with course Id " + cid)
  }
}

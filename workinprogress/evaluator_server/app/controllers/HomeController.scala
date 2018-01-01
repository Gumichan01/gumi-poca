package controllers

import javax.inject._

import model._
import play.api.mvc._

/**
 * This controller creates an `Action` to handle HTTP requests to the
 * application's home page.
 */
@Singleton
class HomeController @Inject()(cc: ControllerComponents) extends AbstractController(cc) {

  val serverInstance = Server

  def index = Action { implicit request =>
    request.session.get("connected").map { user =>
      Server.userWithSurname(user) match {
        case Some(userModel) => Ok(views.html.index("Bienvenue " + user + " sur Evaluator !", true, userModel))
        case _ => Unauthorized("Accès non autorisé. Veuillez vous connecter !")
      }
    }.getOrElse {
      Ok(views.html.index("Bienvenue sur Evaluator !", false))
    }
  }

  def connexion = Action { implicit request =>
    request.session.get("connected").map { user =>
      Redirect("/")
    }.getOrElse {
      Ok(views.html.connexion("Connexion à Evaluator", false, null))
    }
  }

  def inscription = Action { implicit request =>
    request.session.get("connected").map { user =>
      Redirect("/")
    }.getOrElse {
      Ok(views.html.inscription("Inscription", false, null))
    }
  }

  def cours = Action { implicit request =>
    request.session.get("connected").map { user =>
      Server.userWithSurname(user) match {
        case Some(userModel) => Ok(views.html.cours("Profil", true, userModel))
        case _ => Unauthorized("Accès non autorisé. Veuillez vous connecter !")
      }
    }.getOrElse {
      Unauthorized("Accès non autorisé. Veuillez vous connecter !")
    }
  }

  def coursSpecifique(cid: Long) = Action { implicit request =>
    request.session.get("connected").map { user =>
      Server.userWithSurname(user) match {
        case Some(userModel) => Ok(views.html.cours("Profil", true, userModel, cid))
        case _ => Unauthorized("Accès non autorisé. Veuillez vous connecter !")
      }
    }.getOrElse {
      Unauthorized("Accès non autorisé. Veuillez vous connecter !")
    }
  }

  def listeQuestionnaires(cid: Long) = Action { implicit request =>
    request.session.get("connected").map { user =>
      Server.userWithSurname(user) match {
        case Some(userModel) => Ok(views.html.surveys("Liste des questionnaires", true, userModel, cid))
        case _ => Unauthorized("Accès non autorisé. Veuillez vous connecter !")
      }
    }.getOrElse {
      Unauthorized("Accès non autorisé. Veuillez vous connecter !")
    }
  }

  def nouveauQuestionnaire(cid: Long) = Action { implicit request =>
    request.session.get("connected").map { user =>
      val optionalUser = Server.userWithSurname(user)
      optionalUser match {
        case t: Some[Professor] => Ok(views.html.newsurvey("Nouveau Questionnaire", true, t.get, cid))
        case s: Some[Student] => Unauthorized("Accès réservé aux professeurs !")
        case _ => Unauthorized("Accès non autorisé. Veuillez vous connecter !")
      }
    }.getOrElse {
      Unauthorized("Accès non autorisé. Veuillez vous connecter !")
    }
  }

  def utilisateurs = Action { implicit request =>
    request.session.get("connected").map { user =>
      Server.userWithSurname(user) match {
        case Some(userModel) => Ok(views.html.utilisateurs("Utilisateurs", true, userModel, Server.listUsers))
        case _ => Unauthorized("Accès non autorisé. Veuillez vous connecter !")
      }
    }.getOrElse {
      Unauthorized("Accès non autorisé. Veuillez vous connecter !")
    }
  }

  def profil = Action { implicit request =>
    request.session.get("connected").map { user =>
      Server.userWithSurname(user) match {
        case Some(userModel) => Ok(views.html.profil("Profil", true, userModel))
        case _ => Unauthorized("Accès non autorisé. Veuillez vous connecter !")
      }
    }.getOrElse {
      Unauthorized("Accès non autorisé. Veuillez vous connecter !")
    }
  }
}

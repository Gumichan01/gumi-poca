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
      Ok(views.html.index("Bienvenue " + user + " sur Evaluator !", true, user))
    }.getOrElse {
      Ok(views.html.index("Bienvenue sur Evaluator !", false, ""))
    }
  }

  def connexion = Action { implicit request =>
    request.session.get("connected").map { user =>
      Redirect("/")
    }.getOrElse {
      Ok(views.html.connexion("Connexion à Evaluator", false, ""))
    }
  }

  def inscription = Action { implicit request =>
    request.session.get("connected").map { user =>
      Redirect("/")
    }.getOrElse {
      Ok(views.html.inscription("Inscription", false, ""))
    }
  }

  def cours = Action { implicit request =>
    request.session.get("connected").map { user =>
      Ok(views.html.cours("Les cours", true, user))
    }.getOrElse {
      Unauthorized("Accès non autorisé. Veuillez vous connecter !")
    }
  }

  def coursSpecifique(cid: Long) = Action { implicit request =>
    request.session.get("connected").map { user =>
      Ok(views.html.cours("Cours avec id " + cid, true, user, cid))
    }.getOrElse {
      Unauthorized("Accès non autorisé. Veuillez vous connecter !")
    }
  }

  def utilisateurs = Action { implicit request =>
    request.session.get("connected").map { user =>
      Ok(views.html.utilisateurs("Utilisateurs", true, user))
    }.getOrElse {
      Unauthorized("Accès non autorisé. Veuillez vous connecter !")
    }
  }
}

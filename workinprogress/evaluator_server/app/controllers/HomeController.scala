package controllers

import javax.inject._
import play.api.mvc._

/**
 * This controller creates an `Action` to handle HTTP requests to the
 * application's home page.
 */
@Singleton
class HomeController @Inject()(cc: ControllerComponents) extends AbstractController(cc) {

  def index = Action { implicit request =>
    Ok(views.html.index("Bienvenue !"))
  }

  def connexion = Action { implicit request =>
    Ok(views.html.connexion("Connexion à Evaluator"))
  }

  def inscription = Action { implicit request =>
    Ok(views.html.inscription("Inscription"))
  }

  def cours = Action { implicit request =>
    Ok(views.html.cours("Vos cours"))
  }
}

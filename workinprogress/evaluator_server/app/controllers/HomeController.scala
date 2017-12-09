package controllers

import javax.inject._
import play.api.mvc._

/**
 * This controller creates an `Action` to handle HTTP requests to the
 * application's home page.
 */
@Singleton
class HomeController @Inject()(cc: ControllerComponents) extends AbstractController(cc) {

  def index = Action {
    Ok(views.html.index("Your new application is ready."))
  }

  def connexion = Action {
    Ok(views.html.connexion("Connexion à Evaluator"))
  }

  def inscription = Action {
    Ok(views.html.connexion("Inscription"))
  }
}

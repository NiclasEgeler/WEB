package controllers

import javax.inject._
import play.api._
import play.api.mvc._
import de.htwg.se.minesweeper.controller._
import de.htwg.se.minesweeper.controller.modules.DefaultModule.{given}
import de.htwg.se.minesweeper.views.tui.Tui

/** This controller creates an `Action` to handle HTTP requests to the
  * application's home page.
  */
@Singleton
class HomeController @Inject() (val controllerComponents: ControllerComponents)
    extends BaseController {

  var tui = new Tui()
  var controller = new Controller()

  /** Create an Action to render an HTML page.
    *
    * The configuration in the `routes` file means that this method will be
    * called when the application receives a `GET` request with a path of `/`.
    */
  def index() = Action { implicit request: Request[AnyContent] =>
    Ok(views.html.index())
  }

  def game() = Action { implicit request: Request[AnyContent] =>
    Ok(views.html.minesweeper(controller))
  }

  def undo() = Action { implicit request: Request[AnyContent] =>
    controller.undo()
    Ok(views.html.minesweeper(controller))
  }

  def redo() = Action { implicit request: Request[AnyContent] =>
    controller.redo()
    Ok(views.html.minesweeper(controller))
  }

  def flag(x: Int, y: Int) = Action { implicit request: Request[AnyContent] =>
    controller.flagCell(x, y) match {
      case None       => Ok(views.html.minesweeper(controller))
      case Some(grid) => Ok(views.html.minesweeper(controller))
    }
  }

  def place(x: Int, y: Int) = Action { implicit request: Request[AnyContent] =>
    controller.openCell(x, y) match {
      case None       => Ok(views.html.minesweeper(controller))
      case Some(grid) => Ok(views.html.minesweeper(controller))
    } 
  }

  def switchTheme(theme: String) = Action {
    implicit request: Request[AnyContent] =>
      if (theme == "dark" || theme == "white") {
        val newTheme =
          if (theme == "dark") "style-dark.css" else "style-white.css"
        Redirect(routes.HomeController.game())
          .withSession(request.session + ("theme" -> newTheme))
      } else {
        BadRequest("Invalid theme selection")
      }
  }

}

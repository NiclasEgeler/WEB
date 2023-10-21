package controllers

import javax.inject._
import play.api._
import play.api.mvc._
import de.htwg.se.minesweeper.controller._
import de.htwg.se.minesweeper.controller.modules.DefaultModule.{given}
import de.htwg.se.minesweeper.views.tui.Tui

/**
 * This controller creates an `Action` to handle HTTP requests to the
 * application's home page.
 */
@Singleton
class HomeController @Inject()(val controllerComponents: ControllerComponents) extends BaseController {

  var tui = new Tui()
  var controller = new Controller()
  /**
   * Create an Action to render an HTML page.
   *
   * The configuration in the `routes` file means that this method
   * will be called when the application receives a `GET` request with
   * a path of `/`.
   */
  def index() = Action { implicit request: Request[AnyContent] =>
    Ok(views.html.index())
  }

  def game() = Action { implicit request: Request[AnyContent] =>
    Ok(tui.grid(controller.grid))
  }

  def undo() = Action { implicit request: Request[AnyContent] =>
    Ok(tui.grid(controller.undo()))
  }

  def redo() = Action { implicit request: Request[AnyContent] =>
    Ok(tui.grid(controller.redo()))
  }

  def flag(x: Int, y: Int) = Action { implicit request: Request[AnyContent] =>
    controller.flagCell(x, y) match { 
      case None       => Ok(tui.grid(controller.grid))
      case Some(grid) => Ok(tui.grid(grid))
    }      
  }

  def place(x: Int, y:Int) = Action { implicit request: Request[AnyContent] => 
    controller.openCell(x, y) match { 
      case None       => Ok(tui.grid(controller.grid))
      case Some(grid) => Ok(tui.grid(grid))
    }      
  }
  
}

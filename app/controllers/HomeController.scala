package controllers

import javax.inject._
import play.api._
import play.filters.csrf.CSRF
import play.api.mvc._
import de.htwg.se.minesweeper.controller._
import de.htwg.se.minesweeper.controller.modules.DefaultModule.{given}
import de.htwg.se.minesweeper.views.tui.Tui
import play.api.libs.json._
import de.htwg.se.minesweeper.model.cell._
import de.htwg.se.minesweeper.model.grid._

implicit val cellWrites: Writes[ICell] = new Writes [ICell] {
  def writes(cell: ICell) = Json.obj(
    "value" -> cell.getValue,
    "isFlagged" -> cell.isFlagged,
    "isHidden" -> cell.isHidden,
    "isMine" -> cell.isMine,    
  )
}

implicit val gridWrites: Writes[IGrid] = new Writes [IGrid] {
  def writes(grid: IGrid) = Json.obj(
    "columns" -> grid.getWidth,
    "rows" -> grid.getHeight,
    "cells" -> (for (i <- 0 until grid.getHeight) yield grid.getRow(i))
  )
} 

@Singleton
class HomeController @Inject() (val controllerComponents: ControllerComponents)
    extends BaseController {

  var tui = new Tui()
  var controller = new Controller()

  def index() = Action { implicit request: Request[AnyContent] =>
    Ok(views.html.index())
  }

  def game() = Action { implicit request: Request[AnyContent] =>
    Ok(views.html.minesweeper())
  }

  def grid() = Action { implicit request: Request[AnyContent] =>
    Ok(Json.toJson(controller.grid))
  }

  def undo() = Action { implicit request: Request[AnyContent] =>
    controller.undo()
    Ok(Json.toJson(controller.grid))
  }

  def redo() = Action { implicit request: Request[AnyContent] =>
    controller.redo()
    Ok(Json.toJson(controller.grid))
  }

  def flag(x: Int, y: Int) = Action { implicit request: Request[AnyContent] =>
    controller.flagCell(x, y) match {
      case None       => Ok(Json.toJson(controller.grid) )
      case Some(grid) => Ok(Json.toJson(grid))
    } 
  }

  def place(x: Int, y: Int) = Action { implicit request: Request[AnyContent] =>
    controller.openCell(x, y) match {
      case None       => Ok(Json.toJson(controller.grid))
      case Some(grid) => Ok(Json.toJson(grid))
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

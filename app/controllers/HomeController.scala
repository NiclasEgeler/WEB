package controllers

import javax.inject._
import play.api._
import play.filters.csrf.CSRF
import play.api.mvc._
import de.htwg.se.minesweeper.controller._
import de.htwg.se.minesweeper.controller.modules.DefaultModule.{given}
import de.htwg.se.minesweeper.views.tui.Tui
import play.api.libs.json._
import play.api.libs.streams.ActorFlow
import de.htwg.se.minesweeper.model.cell._
import de.htwg.se.minesweeper.model.grid._
import akka.actor.ActorSystem
import akka.stream.Materializer
import akka.actor._
import collection.mutable.ListBuffer


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
class HomeController @Inject() (val controllerComponents: ControllerComponents)(implicit system: ActorSystem, mat: Materializer)
    extends BaseController {

  var tui = new Tui()
  var controller = new Controller()

  var controller_map = Map[String, Controller]()
  var ws_map = Map[String, ActorRef]()
  
  var firstId: String = ""
  var secondId: String = ""

  def index() = Action { implicit request: Request[AnyContent] =>
    Ok(views.html.index())
  }

  def game() = Action { implicit request: Request[AnyContent] =>
    Ok(views.html.minesweeper())
  }

  def multiplayer() = Action { implicit request: Request[AnyContent] =>    
    Ok(views.html.multiplayer())
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
  
  def gridMultiplayer(id: String) = Action { implicit request: Request[AnyContent] =>
    var game = controller_map(id)
    Ok(Json.toJson(game.grid))
  }

  def opponentGrid(id: String) = Action { implicit request: Request[AnyContent] =>
    var game = controller_map(if (id == firstId) secondId else firstId)
    Ok(Json.toJson(game.grid))
  }

  def flagMultiplayer(id: String, x: Int, y: Int) = Action { implicit request: Request[AnyContent] =>    
    var ws = ws_map(if (id == firstId) secondId else firstId)
    var game = controller_map(id)
    var json = game.flagCell(x, y) match {
      case None       => (Json.toJson(game.grid) )
      case Some(grid) => (Json.toJson(grid))
    } 
    ws ! ("update")
    Ok(json)
  }

  def placeMultiplayer(id: String, x: Int, y: Int) = Action { implicit request: Request[AnyContent] =>
    var ws = ws_map(if (id == firstId) secondId else firstId)
    var game = controller_map(id)
    var json = game.openCell(x, y) match {
      case None       => (Json.toJson(game.grid) )
      case Some(grid) => (Json.toJson(grid))
    } 
    ws ! ("update")
    Ok(json)
  }

  def socket(id: String) = WebSocket.accept[String, String] { request =>     
    ActorFlow.actorRef { out =>
      println("Client connected")
      MinesweeperSocketActorFactory.create(out, id)
    }
  }
  
  object MinesweeperSocketActorFactory {
    def create(out: ActorRef, id: String): Props = {
      Props(new MinesweeperSocketActor(out, id))
    }
  }

  class MinesweeperSocketActor(out: ActorRef, id: String) extends Actor{    

    if(firstId == "") {
      firstId = id
      ws_map += (id -> out)   
      controller_map += (id -> new Controller())
      out ! ("wait") 
    } else if(secondId == "") {
      secondId = id
      ws_map += (id -> out)
      controller_map += (id -> new Controller())
      ws_map.foreach { case (key, value) => value ! ("start") }
    } else {
      out ! ("running")
    }

    println(id)
    def receive = {
      case "ping" => out ! ("pong")
      case msg: String =>
        out ! ("I received your message: " + msg)
    }

    def sendGrid() = {
      out ! Json.toJson(controller.grid)
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
package controllers.event

import play.api._
import play.api.mvc._
import play.api.data.Form
import play.api.data.Forms._
import scala.slick.model.codegen.SourceCodeGenerator
import models.Tables.EventRow
import models.{ EventForm, Events }

object EventCreate extends Controller {

  /** イベントフォーム */
  val eventForm = Form(
    mapping(
      "eventId" -> text,
      "eventNm" -> text)(EventForm.apply)(EventForm.unapply))

  /** 初期表示 */
  def index = Action {
    Ok(views.html.event.eventCreate(eventForm))
  }

  /** 登録 */
  def create = Action { implicit request =>
    val form = eventForm.bindFromRequest.get
    val event = EventRow(0, form.eventId, form.eventNm)
    Events.create(event)

    Ok(views.html.event.eventCreate(eventForm))
  }

  /** コード生成 */
  def generate = Action {
    val slickDriver = "scala.slick.driver.H2Driver"
    val outputFolder = "app"
    val pkg = "models"
    val model = Events.model
    new SourceCodeGenerator(model).writeToFile(slickDriver, outputFolder, pkg)

    Ok(views.html.event.eventCreate(eventForm))
  }
}
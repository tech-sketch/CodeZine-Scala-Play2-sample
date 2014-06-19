package models

import play.api.db.DB
import play.api.Play.current
import play.Logger
import scala.slick.driver.H2Driver.simple._
import models.Tables.{ Event, EventRow }
import scala.slick.jdbc.meta.{ MTable, createModel }
import scala.slick.driver.JdbcDriver

object Events {

  /** DBコネクション */
  val database = Database.forDataSource(DB.getDataSource())

  /** 登録 */
  def create(e: EventRow) = database.withTransaction { implicit session: Session =>
    Event.insert(e)
  }

  /** 検索 */
  def find(eventId: String, eventNm: String): List[EventRow] =
    database.withTransaction { implicit session: Session =>

      var q = if (eventId.isEmpty) Event else Event.filter(_.eventId === eventId)
      q = if (eventNm.isEmpty) q else q.filter(_.eventNm like s"%$eventNm%")
      q = q.sortBy(_.eventNm)
      Logger.debug(q.selectStatement)

      return q.list
    }

  def model = database.withSession { implicit session =>
    val tables = MTable.getTables(None, Some("TECHAPP"), None, None).list
    createModel(tables, JdbcDriver)
  }
}
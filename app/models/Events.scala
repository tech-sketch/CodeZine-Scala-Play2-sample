package models

import play.api.db.DB
import play.api.Play.current
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

  def model = database.withSession { implicit session =>
    val tables = MTable.getTables(None, Some("TECHAPP"), None, None).list
    createModel(tables, JdbcDriver)
  }
}
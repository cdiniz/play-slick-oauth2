package models.daos

import java.security.MessageDigest

import com.google.inject.Inject
import models.entities.Account
import models.persistence.SlickTables.AccountsTable
import play.api.db.slick.DatabaseConfigProvider
import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global

trait AccountsDAO extends BaseDAO[AccountsTable,Account]{
  def authenticate(email: String, password: String): Future[Option[Account]]
}

class AccountsDAOImpl @Inject()(override protected val dbConfigProvider: DatabaseConfigProvider) extends AccountsDAO  {

  import dbConfig.driver.api._

  private def digestString(s: String): String = {
    val md = MessageDigest.getInstance("SHA-1")
    md.update(s.getBytes)
    md.digest.foldLeft("") { (s, b) =>
      s + "%02x".format(if (b < 0) b + 256 else b)
    }
  }

  def authenticate(email: String, password: String): Future[Option[Account]] = {
    val hashedPassword = digestString(password)
    findByFilter( acc => acc.password === hashedPassword && acc.email === email).map(_.headOption)
  }
}

package models.daos

import java.sql.Timestamp

import com.google.inject.Inject
import models.entities.OauthAuthorizationCode
import models.persistence.SlickTables.OauthAuthorizationCodeTable
import org.joda.time.DateTime
import play.api.db.slick.DatabaseConfigProvider
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

trait OauthAuthorizationCodesDAO extends BaseDAO[OauthAuthorizationCodeTable,OauthAuthorizationCode]{
  def findByCode(code: String): Future[Option[OauthAuthorizationCode]]
  def delete(code: String): Future[Int]
}

class OauthAuthorizationCodesDAOImpl  @Inject()(override protected val dbConfigProvider: DatabaseConfigProvider) extends OauthAuthorizationCodesDAO {

  import dbConfig.driver.api._


  override def findByCode(code: String): Future[Option[OauthAuthorizationCode]] = {
    val expireAt = new Timestamp(new DateTime().minusMinutes(30).getMillis)
    findByFilter(authCode => authCode.code === code && authCode.createdAt > expireAt).map(_.headOption)
  }

  override def delete(code: String): Future[Int] = deleteByFilter(_.code === code)
}

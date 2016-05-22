package models.daos

import java.security.SecureRandom
import java.sql.Timestamp

import com.google.inject.Inject
import models.entities.{Account, OauthAccessToken, OauthClient}
import models.persistence.SlickTables.OauthAccessTokenTable
import org.joda.time.DateTime
import play.api.db.slick.DatabaseConfigProvider

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.util.Random

trait OauthAccessTokensDAO extends BaseDAO[OauthAccessTokenTable,OauthAccessToken]{
  def create(account: Account, client: OauthClient): Future[OauthAccessToken]
  def delete(account: Account, client: OauthClient): Future[Int]
  def refresh(account: Account, client: OauthClient): Future[OauthAccessToken]
  def findByAccessToken(accessToken: String): Future[Option[OauthAccessToken]]
  def findByAuthorized(account: Account, clientId: String): Future[Option[OauthAccessToken]]
  def findByRefreshToken(refreshToken: String): Future[Option[OauthAccessToken]]
}

class OauthAccessTokensDAOImpl  @Inject()(override protected val dbConfigProvider: DatabaseConfigProvider, oauthClientsDAO: OauthClientsDAO) extends OauthAccessTokensDAO {

  import dbConfig.driver.api._

  override def create(account: Account, client: OauthClient): Future[OauthAccessToken] = {
    def randomString(length: Int) = new Random(new SecureRandom()).alphanumeric.take(length).mkString
    val accessToken = randomString(40)
    val refreshToken = randomString(40)
    val createdAt = new Timestamp(new DateTime().getMillis)
    val oauthAccessToken = new OauthAccessToken(
      id = 0,
      accountId = account.id,
      oauthClientId = client.id,
      accessToken = accessToken,
      refreshToken = refreshToken,
      createdAt = createdAt
    )
    insert(oauthAccessToken).map(id => oauthAccessToken.copy(id = id))
  }

  override def delete(account: Account, client: OauthClient): Future[Int] = {
    deleteByFilter( oauthToken => oauthToken.accountId === account.id && oauthToken.oauthClientId === client.id)
  }

  override def refresh(account: Account, client: OauthClient): Future[OauthAccessToken] = {
    delete(account, client)
    create(account, client)
  }

  override def findByAuthorized(account: Account, clientId: String): Future[Option[OauthAccessToken]] = {
    val query = for {
        oauthClient <- oauthClientsDAO.tableQ
        token <- tableQ if oauthClient.id === token.oauthClientId && oauthClient.clientId === clientId && token.accountId === account.id
      } yield token
    db.run(query.result).map(_.headOption)
  }

  override def findByAccessToken(accessToken: String): Future[Option[OauthAccessToken]] = {
    findByFilter(_.accessToken === accessToken).map(_.headOption)
  }

  override def findByRefreshToken(refreshToken: String): Future[Option[OauthAccessToken]] = {
    val expireAt = new Timestamp(new DateTime().minusMonths(1).getMillis)
    findByFilter( token => token.refreshToken === refreshToken && token.createdAt > expireAt).map(_.headOption)

  }
}
package models.daos

import models.entities.{Account, OauthAccessToken, OauthClient}
import models.persistence.SlickTables.OauthAccessTokenTable
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

trait OauthAccessTokensDAO extends BaseDAO[OauthAccessTokenTable,OauthAccessToken]{
  def create(account: Account, client: OauthClient): Future[OauthAccessToken]
  def delete(account: Account, client: OauthClient): Future[Int]
  def refresh(account: Account, client: OauthClient): Future[OauthAccessToken]
  def findByAccessToken(accessToken: String): Future[Option[OauthAccessToken]]
  def findByAuthorized(account: Account, clientId: String): Future[Option[OauthAccessToken]]
  def findByRefreshToken(refreshToken: String): Future[Option[OauthAccessToken]]
}
package models.daos

import models.entities.{Account, OauthClient}
import models.persistence.SlickTables.OauthClientTable

import scala.concurrent.Future

trait OauthClientsDAO extends BaseDAO[OauthClientTable,OauthClient]{
  def validate(clientId: String, clientSecret: String, grantType: String): Future[Boolean]
  def findByClientId(clientId: String): Future[Option[OauthClient]]
  def findClientCredentials(clientId: String, clientSecret: String): Future[Option[Account]]
}

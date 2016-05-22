package models.daos

import com.google.inject.Inject
import models.entities.{Account, OauthClient}
import models.persistence.SlickTables.OauthClientTable
import play.api.db.slick.DatabaseConfigProvider
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

trait OauthClientsDAO extends BaseDAO[OauthClientTable,OauthClient]{
  def validate(clientId: String, clientSecret: String, grantType: String): Future[Boolean]
  def findByClientId(clientId: String): Future[Option[OauthClient]]
  def findClientCredentials(clientId: String, clientSecret: String): Future[Option[Account]]
}

class OauthClientsDAOImpl @Inject()(override protected val dbConfigProvider: DatabaseConfigProvider, accountsDAO : AccountsDAO) extends OauthClientsDAO  {

  import dbConfig.driver.api._


  override def validate(clientId: String, clientSecret: String, grantType: String): Future[Boolean] = {
    findByFilter(oauthClient => oauthClient.clientId === clientId && oauthClient.clientSecret === clientSecret)
      .map(_.headOption.map(client => grantType == client.grantType || grantType == "refresh_token")
                       .getOrElse(false))
  }

  override def findClientCredentials(clientId: String, clientSecret: String): Future[Option[Account]] = {
    for {
      accountId <- findByFilter(oauthClient => oauthClient.clientId === clientId && oauthClient.clientSecret === clientSecret).map(_.headOption.map(_.ownerId))
      account <- accountsDAO.findById(accountId.get)
    } yield account
  }

  override def findByClientId(clientId: String): Future[Option[OauthClient]] = {
    findByFilter(_.clientId === clientId).map(_.headOption)
  }

}


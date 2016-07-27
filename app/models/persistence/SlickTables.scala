package models.persistence

import java.sql.{Timestamp}

import models.entities._
import play.api.Play
import play.api.db.slick.{DatabaseConfigProvider, HasDatabaseConfig}
import slick.driver.JdbcProfile
import slick.collection.heterogeneous.{ HList, HCons, HNil }
import slick.collection.heterogeneous.syntax._

/**
  * The companion object.
  */
object SlickTables extends HasDatabaseConfig[JdbcProfile] {

  protected lazy val dbConfig = DatabaseConfigProvider.get[JdbcProfile](Play.current)
  import dbConfig.driver.api._

  abstract class BaseTable[T](tag: Tag, name: String) extends Table[T](tag, name) {
    def id = column[Long]("id", O.PrimaryKey, O.AutoInc)
    def createdAt = column[Timestamp]("created_at")
  }

  type AccountHList = Long :: String :: String :: Timestamp :: Long :: Long :: Long :: Long :: Long :: Long :: Long :: Long :: Long :: Long :: Long :: Long :: Long :: Long :: Long :: Long :: Long :: Long :: Long :: HNil

  class AccountsTable(tag : Tag) extends BaseTable[Account](tag, "accounts") {
    def email = column[String]("email")
    def password = column[String]("password")
    def field5 = column[Long]("field5")
    def field6 = column[Long]("field6")
    def field7 = column[Long]("field7")
    def field8 = column[Long]("field8")
    def field9 = column[Long]("field9")
    def field10 = column[Long]("field10")
    def field11 = column[Long]("field11")
    def field12 = column[Long]("field12")
    def field13 = column[Long]("field13")
    def field14 = column[Long]("field14")
    def field15 = column[Long]("field15")
    def field16 = column[Long]("field16")
    def field17 = column[Long]("field17")
    def field18 = column[Long]("field18")
    def field19 = column[Long]("field19")
    def field20 = column[Long]("field20")
    def field21 = column[Long]("field21")
    def field22 = column[Long]("field22")
    def field23 = column[Long]("field23")
    def * = id :: email :: password :: createdAt :: field5 :: field6 :: field7 :: field8 :: field9 :: field10 :: field11 :: field12 :: field13 :: field14 :: field15 :: field16 :: field17 :: field18 :: field19 :: field20 :: field21 :: field22 :: field23 ::  HNil <> (createAccount, extractAccount)
  }

  // Mapping from HList to case class:
  def createAccount(data: AccountHList): Account = data match {
    case id :: email :: password :: createdAt :: field5 :: field6 :: field7 :: field8 :: field9 :: field10 :: field11 :: field12 :: field13 :: field14 :: field15 :: field16 :: field17 :: field18 :: field19 :: field20 :: field21 :: field22 :: field23 :: HNil =>
      Account(id, email, password, createdAt, field5, field6, field7, field8, field9, field10, field11, field12, field13, field14, field15, field16, field17, field18, field19, field20, field21, field22, field23)
  }
  // Mapping from case class to Hlist
  def extractAccount(account: Account): Option[AccountHList] = account match {
    case Account(id,email,password,createdAt, field5, field6, field7, field8, field9, field10, field11, field12, field13, field14, field15, field16, field17, field18, field19, field20, field21, field22, field23)
    => Some(id :: email :: password :: createdAt :: field5 :: field6 :: field7 :: field8 :: field9 :: field10 :: field11 :: field12 :: field13 :: field14 :: field15 :: field16 :: field17 :: field18 :: field19 :: field20 :: field21 :: field22 :: field23 :: HNil)
  }

  implicit val accountsTableQ : TableQuery[AccountsTable] = TableQuery[AccountsTable]

  class OauthClientTable(tag : Tag) extends BaseTable[OauthClient](tag,"oauth_clients") {
    def ownerId = column[Long]("owner_id")
    def grantType = column[String]("grant_type")
    def clientId = column[String]("client_id")
    def clientSecret = column[String]("client_secret")
    def redirectUri = column[Option[String]]("redirect_uri")
    def * = (id, ownerId, grantType, clientId, clientSecret, redirectUri, createdAt) <> (OauthClient.tupled, OauthClient.unapply)

    def owner = foreignKey(
      "oauth_client_account_fk",
      ownerId,
      accountsTableQ)(_.id)
  }

  implicit val OauthClientTableQ : TableQuery[OauthClientTable] = TableQuery[OauthClientTable]

  class OauthAuthorizationCodeTable(tag : Tag) extends BaseTable[OauthAuthorizationCode](tag,"oauth_authorization_codes") {
    def accountId = column[Long]("account_id")
    def oauthClientId = column[Long]("oauth_client_id")
    def code = column[String]("code")
    def redirectUri = column[Option[String]]("redirect_uri")
    def * = (id, accountId, oauthClientId, code, redirectUri, createdAt) <> (OauthAuthorizationCode.tupled, OauthAuthorizationCode.unapply)

    def account = foreignKey(
      "oauth_authorization_code_account_fk",
      accountId,
      accountsTableQ)(_.id)

    def oauthClient = foreignKey(
      "oauth_authorization_code_client_fk",
      oauthClientId,
      OauthClientTableQ)(_.id)
  }

  implicit val OauthAuthorizationCodeTableQ : TableQuery[OauthAuthorizationCodeTable] = TableQuery[OauthAuthorizationCodeTable]

  class OauthAccessTokenTable(tag : Tag) extends BaseTable[OauthAccessToken](tag,"oauth_access_tokens") {
    def accountId = column[Long]("account_id")
    def oauthClientId = column[Long]("oauth_client_id")
    def accessToken = column[String]("access_token")
    def refreshToken = column[String]("refresh_token")
    def * = (id, accountId, oauthClientId, accessToken, refreshToken, createdAt) <> (OauthAccessToken.tupled, OauthAccessToken.unapply)

    def account = foreignKey(
      "oauth_access_token_account_fk",
      accountId,
      accountsTableQ)(_.id)

    def oauthClient = foreignKey(
      "oauth_access_token_client_fk",
      oauthClientId,
      OauthAuthorizationCodeTableQ)(_.id)

  }

  implicit val OauthAccessTokenTableQ : TableQuery[OauthAccessTokenTable] = TableQuery[OauthAccessTokenTable]

}

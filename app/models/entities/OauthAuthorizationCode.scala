package models.entities

case class OauthAuthorizationCode(
                                   id: Long,
                                   accountId: Long,
                                   oauthClientId: Long,
                                   code: String,
                                   redirectUri: Option[String],
                                   createdAt: java.sql.Timestamp) extends BaseEntity


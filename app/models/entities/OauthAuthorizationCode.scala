package models.entities

import java.sql.Timestamp

case class OauthAuthorizationCode(
                                   id: Long,
                                   accountId: Long,
                                   oauthClientId: Long,
                                   code: String,
                                   redirectUri: Option[String],
                                   createdAt: Timestamp) extends BaseEntity


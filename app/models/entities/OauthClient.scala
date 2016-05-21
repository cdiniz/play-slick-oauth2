package models.entities

import java.sql.Timestamp

case class OauthClient(
                        id: Long,
                        ownerId: Long,
                        grantType: String,
                        clientId: String,
                        clientSecret: String,
                        redirectUri: Option[String],
                        createdAt: Timestamp
                      ) extends BaseEntity


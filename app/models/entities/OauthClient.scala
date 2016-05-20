package models.entities

case class OauthClient(
                        id: Long,
                        ownerId: Long,
                        grantType: String,
                        clientId: String,
                        clientSecret: String,
                        redirectUri: Option[String],
                        createdAt: java.sql.Timestamp
                      ) extends BaseEntity


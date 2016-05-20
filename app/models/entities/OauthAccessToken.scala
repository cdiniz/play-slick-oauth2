package models.entities


case class OauthAccessToken(
                             id: Long,
                             accountId: Long,
                             oauthClientId: Long,
                             accessToken: String,
                             refreshToken: String,
                             createdAt: java.sql.Timestamp
                           )

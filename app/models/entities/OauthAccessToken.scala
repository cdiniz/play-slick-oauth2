package models.entities

import java.sql.Timestamp

case class OauthAccessToken(
                             id: Long,
                             accountId: Long,
                             oauthClientId: Long,
                             accessToken: String,
                             refreshToken: String,
                             createdAt: Timestamp
                           ) extends BaseEntity
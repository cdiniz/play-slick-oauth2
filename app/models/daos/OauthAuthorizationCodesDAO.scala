package models.daos

import models.entities.{OauthAuthorizationCode}
import models.persistence.SlickTables.{OauthAuthorizationCodeTable}

trait OauthAuthorizationCodesDAO extends BaseDAO[OauthAuthorizationCodeTable,OauthAuthorizationCode]{
  def findByCode(code: String): Option[OauthAuthorizationCode]
  def delete(code: String): Unit
}

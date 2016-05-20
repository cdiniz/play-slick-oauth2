package models.entities

import org.joda.time.DateTime

case class Account(id: Long, email: String, password: String, createdAt: java.sql.Timestamp) extends BaseEntity

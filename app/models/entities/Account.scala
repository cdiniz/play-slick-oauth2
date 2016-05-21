package models.entities

import java.sql.Timestamp

case class Account(id: Long, email: String, password: String, createdAt: Timestamp) extends BaseEntity

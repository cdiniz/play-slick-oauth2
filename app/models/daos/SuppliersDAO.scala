package models.daos

import com.google.inject.Inject
import models.entities.Supplier
import models.persistence.SlickTables.SuppliersTable
import play.api.db.slick.DatabaseConfigProvider

trait SuppliersDAO extends BaseDAO[SuppliersTable,Supplier]

class SuppliersDAOImpl @Inject() (override protected val dbConfigProvider: DatabaseConfigProvider) extends SuppliersDAO

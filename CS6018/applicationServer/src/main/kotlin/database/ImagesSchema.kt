package com.example.database

import kotlinx.coroutines.Dispatchers
import kotlinx.serialization.Serializable
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import org.jetbrains.exposed.sql.transactions.transaction

@Serializable
data class Image(val id: String, val owner: String, val path: String)

class ImageService(database: Database) {
    object Images : Table() {
        val id = varchar("id", length = 40)
        val owner = varchar("owner", length = 100)
        val path = varchar("path", length = 256)

        override val primaryKey = PrimaryKey(id)
    }

    init {
        transaction(database) {
            SchemaUtils.create(Images)
        }
    }

    suspend fun create(img: Image): String = dbQuery {
        Images.insert {
            it[id] = img.id
            it[owner] = img.owner
            it[path] = img.path
        }[Images.id]
    }

    suspend fun read(owner: String): List<Image> {
        return dbQuery {
            Images.selectAll()
                .where { Images.owner eq owner }
                .map {
                    Image(
                        it[Images.id],
                        it[Images.owner],
                        it[Images.path]
                    )
                }
        }
    }

    private suspend fun <T> dbQuery(block: suspend () -> T): T =
        newSuspendedTransaction(Dispatchers.IO) { block() }
}


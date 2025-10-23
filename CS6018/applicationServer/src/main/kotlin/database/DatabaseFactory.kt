package com.example.database

import io.ktor.server.application.ApplicationEnvironment
import org.jetbrains.exposed.sql.Database

object DatabaseFactory {
    fun init(driver: String, url: String, user: String?, password: String?): Database {
        return Database.connect(
            url = url,
            driver = driver,
            user = user ?: "",
            password = password ?: ""
        )
    }
}

fun dbFromEnv(environment: ApplicationEnvironment): Database {
    val cfg = environment.config
    return DatabaseFactory.init(
        driver = cfg.property("db.driver").getString(),
        url = cfg.property("db.url").getString(),
        user = cfg.propertyOrNull("db.user")?.getString(),
        password = cfg.propertyOrNull("db.password")?.getString()
    )
}

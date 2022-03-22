package com.interview

import io.javalin.Javalin
import org.jetbrains.exposed.sql.Database
import org.slf4j.Logger
import org.slf4j.LoggerFactory

val log: Logger = LoggerFactory.getLogger("app")

val app: Javalin
    get() = Javalin.create().also { app ->
        app.post("/clients") { ctx -> Clients.importCsv(ctx.bodyAsInputStream()) }
        app.post("/appointments") { ctx -> Appointments.importCsv(ctx.bodyAsInputStream()) }
        app.post("/services") { ctx -> Services.importCsv(ctx.bodyAsInputStream()) }
        app.post("/purchases") { ctx -> Purchases.importCsv(ctx.bodyAsInputStream()) }

        app.get("/clients/top", ::getTopClients)

        app.exception(RuntimeException::class.java) { e, ctx ->
            log.error("Runtime Exception:", e)
            ctx.status(500)
        }
    }

fun main() {
    connectToDatabase()
    app.start(7070)
}

fun connectToDatabase() {
    Database.connect("jdbc:postgresql:postgres", user = "postgres", password = "password")
}

package com.interview

import io.javalin.http.Context
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.transactions.transaction
import java.util.UUID


fun getClient(ctx: Context) {
    val id = ctx.pathParamAsClass("id", UUID::class.java).get()

    val clientRow = transaction {
        Clients.select { Clients.id eq id }.singleOrNull()
    }

    if (clientRow != null) {
        ctx.json(toClient(clientRow))
    } else {
        ctx.status(404)
    }
}

private fun toClient(it: ResultRow) = Client(
    id = it[Clients.id].value,
    firstName = it[Clients.firstName],
    lastName = it[Clients.lastName],
    email = it[Clients.email],
    phone = it[Clients.phone],
    gender = it[Clients.gender],
    banned = it[Clients.banned]
)

data class Client(
    val id: UUID,
    val firstName: String,
    val lastName: String,
    val email: String,
    val phone: String,
    val gender: Gender,
    val banned: Boolean
)

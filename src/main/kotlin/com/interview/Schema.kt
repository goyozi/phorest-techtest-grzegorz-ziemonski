package com.interview

import org.jetbrains.exposed.dao.id.UUIDTable
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.javatime.timestamp
import org.jetbrains.exposed.sql.transactions.transaction
import org.postgresql.jdbc.PgConnection
import java.io.InputStream

object Clients : UUIDTable("clients") {
    val firstName = varchar("first_name", 255)
    val lastName = varchar("last_name", 255)
    val email = varchar("email", 255)
    val phone = varchar("phone", 255)
    val gender = customEnumeration("gender", "Gender", { value -> Gender.valueOf(value as String) }, { PGEnum("Gender", it) })
    val banned = bool("banned")
}

object Appointments : UUIDTable("appointments") {
    val clientId = uuid("client_id").references(Clients.id)
    val startTime = timestamp("start_time")
    val endTime = timestamp("end_time")
}

object Services : UUIDTable("services") {
    val appointmentId = uuid("appointment_id").references(Appointments.id)
    val name = varchar("name", 255)
    val price = decimal("price", 10, 2)
    val loyaltyPoints = integer("loyalty_points")
}

object Purchases : UUIDTable("purchases") {
    val appointmentId = uuid("appointment_id").references(Appointments.id)
    val name = varchar("name", 255)
    val price = decimal("price", 10, 2)
    val loyaltyPoints = integer("loyalty_points")
}

enum class Gender {
    Male, Female
}

fun Table.importCsv(csv: InputStream) {
    transaction {
        (connection.connection as PgConnection).copyInto(tableName, csv)
    }
}

package com.interview

import io.javalin.plugin.json.JavalinJackson
import org.jetbrains.exposed.sql.deleteAll
import org.jetbrains.exposed.sql.transactions.transaction
import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.BeforeAll

abstract class IntegrationTest {
    val jackson = JavalinJackson()

    companion object {

        @JvmStatic
        @BeforeAll
        fun setUpAll() {
            connectToDatabase()
            cleanUp()
        }

        @JvmStatic
        @AfterAll
        fun cleanUp() {
            transaction {
                Purchases.deleteAll()
                Services.deleteAll()
                Appointments.deleteAll()
                Clients.deleteAll()
            }
        }
    }
}
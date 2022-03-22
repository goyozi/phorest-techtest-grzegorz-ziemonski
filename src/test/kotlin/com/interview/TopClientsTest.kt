package com.interview

import io.javalin.testtools.TestUtil.test
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class TopClientsTest : IntegrationTest() {

    @BeforeEach
    fun setUp() {
        cleanUp()
    }

    @Test
    fun `no clients`() = test(app) { _, _ ->
        assertThat(topClients(10, since = beforeAppointment))
            .isEqualTo(emptyList<TopClient>())
    }

    @Test
    fun `client with zero points`() = test(app) { _, http ->
        val clientId = http.createClient()
        http.createAppointment(clientId)

        assertThat(topClients(10, since = beforeAppointment))
            .isEqualTo(listOf(TopClient(clientId, 0)))
    }

    @Test
    fun `client with only service points`() = test(app) { _, http ->
        val clientId = http.createClient()
        val appointmentId = http.createAppointment(clientId)
        http.createService(appointmentId, serviceLoyaltyPoints)

        assertThat(topClients(10, since = beforeAppointment))
            .isEqualTo(listOf(TopClient(clientId, serviceLoyaltyPoints)))
    }

    @Test
    fun `client with only purchase points`() = test(app) { _, http ->
        val clientId = http.createClient()
        val appointmentId = http.createAppointment(clientId)
        http.createPurchase(appointmentId, purchaseLoyaltyPoints)

        assertThat(topClients(10, since = beforeAppointment))
            .isEqualTo(listOf(TopClient(clientId, purchaseLoyaltyPoints)))
    }

    @Test
    fun `client with both service and purchase points`() = test(app) { _, http ->
        val clientId = http.createClient()
        val appointmentId = http.createAppointment(clientId)
        http.createService(appointmentId, serviceLoyaltyPoints)
        http.createPurchase(appointmentId, purchaseLoyaltyPoints)

        assertThat(topClients(10, since = beforeAppointment))
            .isEqualTo(listOf(TopClient(clientId, serviceLoyaltyPoints + purchaseLoyaltyPoints)))
    }

    @Test
    fun `client with older appointments`() = test(app) { _, http ->
        val clientId = http.createClient()
        http.createAppointment(clientId)

        assertThat(topClients(10, since = afterAppointment))
            .isEqualTo(emptyList<TopClient>())
    }

    @Test
    fun `banned client`() = test(app) { _, http ->
        val clientId = http.createClient(banned = true)
        http.createAppointment(clientId)

        assertThat(topClients(10, since = beforeAppointment))
            .isEqualTo(emptyList<TopClient>())
    }

    @Test
    fun `sorting clients by points`() = test(app) { _, http ->
        val firstClientId = http.createClient()
        val firstAppointmentId = http.createAppointment(firstClientId)
        http.createPurchase(firstAppointmentId, 100)

        val secondClientId = http.createClient()
        val secondAppointmentId = http.createAppointment(secondClientId)
        http.createService(secondAppointmentId, 200)

        assertThat(topClients(10, since = beforeAppointment))
            .isEqualTo(listOf(TopClient(secondClientId, 200), TopClient(firstClientId, 100)))
    }

    @Test
    fun `result limit`() = test(app) { _, http ->
        val firstClientId = http.createClient()
        val firstAppointmentId = http.createAppointment(firstClientId)
        http.createPurchase(firstAppointmentId, 100)

        val secondClientId = http.createClient()
        val secondAppointmentId = http.createAppointment(secondClientId)
        http.createService(secondAppointmentId, 200)

        assertThat(topClients(1, since = beforeAppointment))
            .isEqualTo(listOf(TopClient(secondClientId, 200)))
    }
}
import io.javalin.testtools.TestUtil.test
import org.assertj.core.api.Assertions.assertThat
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation
import org.junit.jupiter.api.Order
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestMethodOrder

@TestMethodOrder(OrderAnnotation::class)
class CsvImportTest {

    @Test
    @Order(1)
    fun `client import`() = test(app) { _, http ->
        http.postCsv("/clients", clientsCsv)

        val client = transaction { Clients.selectAll().single() }
        assertThat(client[Clients.id].value).isEqualTo(clientId)
        assertThat(client[Clients.firstName]).isEqualTo(firstName)
        assertThat(client[Clients.lastName]).isEqualTo(lastName)
        assertThat(client[Clients.email]).isEqualTo(email)
        assertThat(client[Clients.phone]).isEqualTo(phone)
        assertThat(client[Clients.gender]).isEqualTo(gender)
        assertThat(client[Clients.banned]).isEqualTo(banned)
    }

    @Test
    @Order(2)
    fun `appointment import`() = test(app) { _, http ->
        http.postCsv("/appointments", appointmentsCsv)

        val appointment = transaction { Appointments.selectAll().single() }
        assertThat(appointment[Appointments.id].value).isEqualTo(appointmentId)
        assertThat(appointment[Appointments.clientId]).isEqualTo(clientId)
        assertThat(appointment[Appointments.startTime]).isEqualTo(startTime)
        assertThat(appointment[Appointments.endTime]).isEqualTo(endTime)
    }

    @Test
    @Order(3)
    fun `services import`() = test(app) { _, http ->
        http.postCsv("/services", servicesCsv)

        val service = transaction { Services.selectAll().single() }
        assertThat(service[Services.id].value).isEqualTo(serviceId)
        assertThat(service[Services.appointmentId]).isEqualTo(appointmentId)
        assertThat(service[Services.name]).isEqualTo(serviceName)
        assertThat(service[Services.price]).isEqualTo(servicePrice.setScale(2))
        assertThat(service[Services.loyaltyPoints]).isEqualTo(serviceLoyaltyPoints)
    }

    @Test
    @Order(3)
    fun `purchase import`() = test(app) { _, http ->
        http.postCsv("/purchases", purchasesCsv)

        val purchase = transaction { Purchases.selectAll().single() }
        assertThat(purchase[Purchases.id].value).isEqualTo(purchaseId)
        assertThat(purchase[Purchases.appointmentId]).isEqualTo(appointmentId)
        assertThat(purchase[Purchases.name]).isEqualTo(purchaseName)
        assertThat(purchase[Purchases.price]).isEqualTo(purchasePrice.setScale(2))
        assertThat(purchase[Purchases.loyaltyPoints]).isEqualTo(purchaseLoyaltyPoints)
    }
}

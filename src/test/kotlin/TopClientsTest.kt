import io.javalin.testtools.HttpClient
import io.javalin.testtools.TestUtil.test
import org.assertj.core.api.Assertions.assertThat
import org.jetbrains.exposed.sql.deleteAll
import org.jetbrains.exposed.sql.transactions.transaction
import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.util.UUID

class TopClientsTest {

    @BeforeEach
    fun setUp() {
        cleanUp()
    }

    @Test
    fun `client with zero points`() = test(app) { _, http ->
        val clientId = http.createClient()
        http.createAppointment(clientId)

        assertThat(getTopClients(10, since = beforeAppointment))
            .isEqualTo(listOf(TopClient(clientId, 0)))
    }

    @Test
    fun `client with only service points`() = test(app) { _, http ->
        val clientId = http.createClient()
        val appointmentId = http.createAppointment(clientId)
        http.createService(appointmentId, serviceLoyaltyPoints)

        assertThat(getTopClients(10, since = beforeAppointment))
            .isEqualTo(listOf(TopClient(clientId, serviceLoyaltyPoints)))
    }

    @Test
    fun `client with only purchase points`() = test(app) { _, http ->
        val clientId = http.createClient()
        val appointmentId = http.createAppointment(clientId)
        http.createPurchase(appointmentId, purchaseLoyaltyPoints)

        assertThat(getTopClients(10, since = beforeAppointment))
            .isEqualTo(listOf(TopClient(clientId, purchaseLoyaltyPoints)))
    }

    @Test
    fun `client with both service and purchase points`() = test(app) { _, http ->
        val clientId = http.createClient()
        val appointmentId = http.createAppointment(clientId)
        http.createService(appointmentId, serviceLoyaltyPoints)
        http.createPurchase(appointmentId, purchaseLoyaltyPoints)

        assertThat(getTopClients(10, since = beforeAppointment))
            .isEqualTo(listOf(TopClient(clientId, serviceLoyaltyPoints + purchaseLoyaltyPoints)))
    }

    @Test
    fun `client with older appointments`() = test(app) { _, http ->
        val clientId = http.createClient()
        http.createAppointment(clientId)

        assertThat(getTopClients(10, since = afterAppointment))
            .isEqualTo(emptyList<TopClient>())
    }

    @Test
    fun `banned client`() = test(app) { _, http ->
        val clientId = http.createClient(banned = true)
        http.createAppointment(clientId)

        assertThat(getTopClients(10, since = beforeAppointment))
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

        assertThat(getTopClients(10, since = beforeAppointment))
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

        assertThat(getTopClients(1, since = beforeAppointment))
            .isEqualTo(listOf(TopClient(secondClientId, 200)))
    }

    private fun HttpClient.createClient(banned: Boolean = false): UUID {
        val clientId = UUID.randomUUID()
        postCsv(
            path = "/clients",
            csv = """
                $clientsCsvHeader,
                $clientId,$firstName,$lastName,$email,$phone,$gender,$banned
            """.trimIndent()
        )
        return clientId
    }

    private fun HttpClient.createAppointment(clientId: UUID): UUID {
        val appointmentId = UUID.randomUUID()
        postCsv(
            path = "/appointments",
            csv = """
                $appointmentsCsvHeader,
                $appointmentId,$clientId,$startTimeCsv,$endTimeCsv
            """.trimIndent()
        )
        return appointmentId
    }

    private fun HttpClient.createService(appointmentId: UUID, points: Int) {
        postCsv(
            path = "/services",
            csv = """
                $servicesCsvHeader,
                $serviceId,$appointmentId,$serviceName,$servicePrice,$points
            """.trimIndent()
        )
    }

    private fun HttpClient.createPurchase(appointmentId: UUID, points: Int) {
        postCsv(
            path = "/purchases",
            csv = """
                $purchasesCsvHeader,
                $purchaseId,$appointmentId,$purchaseName,$purchasePrice,$points
            """.trimIndent()
        )
    }

    companion object {

        @JvmStatic
        @BeforeAll
        fun setUpAll() {
            connectToDatabase()
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
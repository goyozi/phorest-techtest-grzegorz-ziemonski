import Clients.banned
import Clients.email
import Clients.firstName
import Clients.gender
import Clients.id
import Clients.lastName
import Clients.phone
import Gender.Female
import io.javalin.testtools.HttpClient
import io.javalin.testtools.TestUtil.test
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.toRequestBody
import org.assertj.core.api.Assertions.assertThat
import org.jetbrains.exposed.sql.deleteAll
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.util.UUID

class AppTest {

    @BeforeEach
    fun setUp() {
        connectToDatabase()
        transaction { Clients.deleteAll() }
    }

    @Test
    fun `clients import`() = test(app) { _, http ->
        val clientsCsv = """
            id,first_name,last_name,email,phone,gender,banned
            e0b8ebfc-6e57-4661-9546-328c644a3764,Dori,Dietrich,patrica@keeling.net,(272) 301-6356,Female,false
        """.trimIndent()

        val response = http.postCsv("/clients", clientsCsv)
        assertThat(response.code).isEqualTo(200)

        val client = transaction { Clients.selectAll().single() }
        assertThat(client[id].value).isEqualTo(UUID.fromString("e0b8ebfc-6e57-4661-9546-328c644a3764"))
        assertThat(client[firstName]).isEqualTo("Dori")
        assertThat(client[lastName]).isEqualTo("Dietrich")
        assertThat(client[email]).isEqualTo("patrica@keeling.net")
        assertThat(client[phone]).isEqualTo("(272) 301-6356")
        assertThat(client[gender]).isEqualTo(Female)
        assertThat(client[banned]).isFalse
    }

    private fun HttpClient.postCsv(path: String, csv: String) = this.request(path) {
        it.method("POST", csv.toRequestBody("text/csv".toMediaType()))
    }
}

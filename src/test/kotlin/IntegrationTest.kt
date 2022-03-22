import org.jetbrains.exposed.sql.deleteAll
import org.jetbrains.exposed.sql.transactions.transaction
import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.BeforeAll

abstract class IntegrationTest {

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
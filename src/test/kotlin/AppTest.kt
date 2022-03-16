import io.javalin.testtools.TestUtil.test
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class AppTest {

    @Test
    fun `setup works`() = test(app) { _, client ->
        assertThat(client.get("/").body?.string()).isEqualTo("Hello World")
    }
}

import io.javalin.testtools.HttpClient
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.toRequestBody
import org.assertj.core.api.Assertions.assertThat

fun HttpClient.postCsv(path: String, csv: String) {
    val response = request(path) {
        it.method("POST", csv.toRequestBody("text/csv".toMediaType()))
    }
    assertThat(response.code).isEqualTo(200)
}

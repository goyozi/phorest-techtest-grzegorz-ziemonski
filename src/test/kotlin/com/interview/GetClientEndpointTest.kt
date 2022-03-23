package com.interview

import io.javalin.testtools.TestUtil
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class GetClientEndpointTest : IntegrationTest() {

    @BeforeEach
    fun setUp() {
        cleanUp()
    }

    @Test
    fun `client found`() = TestUtil.test(app) { _, http ->
        val clientId = http.createClient()

        val response = http.get("/clients/$clientId")

        assertThat(response.code).isEqualTo(200)
        assertThat(jackson.fromJsonString(response.body!!.string(), Client::class.java))
            .isEqualTo(Client(clientId, firstName, lastName, email, phone, gender, banned))
    }

    @Test
    fun `client not found`() = TestUtil.test(app) { _, http ->
        val response = http.get("/clients/$clientId")

        assertThat(response.code).isEqualTo(404)
    }

    @Test
    fun `invalid client id`() = TestUtil.test(app) { _, http ->
        val response = http.get("/clients/invalid")

        assertThat(response.code).isEqualTo(400)
        assertThat(response.body!!.string()).isEqualTo("""{"id":[{"message":"TYPE_CONVERSION_FAILED","args":{},"value":"invalid"}]}""")
    }
}
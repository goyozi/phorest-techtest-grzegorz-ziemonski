package com.interview

import io.javalin.testtools.TestUtil.test
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class TopClientsEndpointTest : IntegrationTest() {

    @Test
    fun `happy path`() = test(app) { _, http ->
        val clientId = http.createClient()
        val appointmentId = http.createAppointment(clientId)
        http.createService(appointmentId, serviceLoyaltyPoints)

        val response = http.get("/clients/top?number=10&since=$beforeAppointment")

        assertThat(response.code).isEqualTo(200)
        assertThat(jackson.fromJsonString(response.body!!.string(), Array<TopClient>::class.java))
            .isEqualTo(arrayOf(TopClient(clientId, serviceLoyaltyPoints)))
    }

    @Test
    fun `missing number`() = test(app) { _, http ->
        val response = http.get("/clients/top?since=$beforeAppointment")

        assertThat(response.code).isEqualTo(400)
        assertThat(response.body!!.string()).isEqualTo("""{"number":[{"message":"NULLCHECK_FAILED","args":{},"value":null}]}""")
    }

    @Test
    fun `negative number`() = test(app) { _, http ->
        val response = http.get("/clients/top?number=-1&since=$beforeAppointment")

        assertThat(response.code).isEqualTo(400)
        assertThat(response.body!!.string()).isEqualTo("""{"number":[{"message":"POSITIVE_CHECK_FAILED","args":{},"value":-1}]}""")
    }

    @Test
    fun `zero-value number`() = test(app) { _, http ->
        val response = http.get("/clients/top?number=0&since=$beforeAppointment")

        assertThat(response.code).isEqualTo(400)
        assertThat(response.body!!.string()).isEqualTo("""{"number":[{"message":"POSITIVE_CHECK_FAILED","args":{},"value":0}]}""")
    }

    @Test
    fun `not a number`() = test(app) { _, http ->
        val response = http.get("/clients/top?number=NaN&since=$beforeAppointment")

        assertThat(response.code).isEqualTo(400)
        assertThat(response.body!!.string()).isEqualTo("""{"number":[{"message":"TYPE_CONVERSION_FAILED","args":{},"value":"NaN"}]}""")
    }

    @Test
    fun `missing since date`() = test(app) { _, http ->
        val response = http.get("/clients/top?number=10")

        assertThat(response.code).isEqualTo(400)
        assertThat(response.body!!.string()).isEqualTo("""{"since":[{"message":"NULLCHECK_FAILED","args":{},"value":null}]}""")
    }
}

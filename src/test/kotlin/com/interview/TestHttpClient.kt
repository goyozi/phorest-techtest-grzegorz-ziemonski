package com.interview

import io.javalin.testtools.HttpClient
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.toRequestBody
import org.assertj.core.api.Assertions.assertThat
import java.util.UUID


fun HttpClient.createClient(banned: Boolean = false): UUID {
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

fun HttpClient.createAppointment(clientId: UUID): UUID {
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

fun HttpClient.createService(appointmentId: UUID, points: Int) {
    postCsv(
        path = "/services",
        csv = """
            $servicesCsvHeader,
            $serviceId,$appointmentId,$serviceName,$servicePrice,$points
        """.trimIndent()
    )
}

fun HttpClient.createPurchase(appointmentId: UUID, points: Int) {
    postCsv(
        path = "/purchases",
        csv = """
            $purchasesCsvHeader,
            $purchaseId,$appointmentId,$purchaseName,$purchasePrice,$points
        """.trimIndent()
    )
}

fun HttpClient.postCsv(path: String, csv: String) {
    val response = request(path) {
        it.method("POST", csv.toRequestBody("text/csv".toMediaType()))
    }
    assertThat(response.code).isEqualTo(200)
}

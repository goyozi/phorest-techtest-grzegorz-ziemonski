import java.time.Instant
import java.util.UUID

val clientId = UUID.fromString("e0b8ebfc-6e57-4661-9546-328c644a3764")
val firstName = "Dori"
val lastName = "Dietrich"
val email = "patrica@keeling.net"
val phone = "(272) 301-6356"
val gender = Gender.Female
val banned = false

val clientsCsv = """
    id,first_name,last_name,email,phone,gender,banned
    $clientId,$firstName,$lastName,$email,$phone,$gender,$banned
""".trimIndent()

val appointmentId = UUID.fromString("7416ebc3-12ce-4000-87fb-82973722ebf4")
val startTimeCsv = "2016-02-07 17:15:00 +0000"
val startTime = Instant.parse("2016-02-07T17:15:00Z")
val endTimeCsv = "2016-02-07 20:15:00 +0000"
val endTime = Instant.parse("2016-02-07T20:15:00Z")

val appointmentsCsv = """
    id,client_id,start_time,end_time
    $appointmentId,$clientId,$startTimeCsv,$endTimeCsv
""".trimIndent()

val serviceId = UUID.fromString("f1fc7009-0c44-4f89-ac98-5de9ce58095c")
val serviceName = "Full Head Colour"
val servicePrice = "85.0".toBigDecimal()
val serviceLoyaltyPoints = 80

val servicesCsv = """
    id,appointment_id,name,price,loyalty_points
    $serviceId,$appointmentId,$serviceName,$servicePrice,$serviceLoyaltyPoints
""".trimIndent()

val purchaseId = UUID.fromString("d2d3b92d-f9b5-48c5-bf31-88c28e3b73ac")
val purchaseName = "Shampoo"
val purchasePrice = "19.5".toBigDecimal()
val purchaseLoyaltyPoints = 20

val purchasesCsv = """
    id,appointment_id,name,price,loyalty_points
    $purchaseId,$appointmentId,$purchaseName,$purchasePrice,$purchaseLoyaltyPoints
""".trimIndent()
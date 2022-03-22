import org.jetbrains.exposed.sql.SortOrder.DESC
import org.jetbrains.exposed.sql.SqlExpressionBuilder.coalesce
import org.jetbrains.exposed.sql.SqlExpressionBuilder.plus
import org.jetbrains.exposed.sql.andWhere
import org.jetbrains.exposed.sql.booleanLiteral
import org.jetbrains.exposed.sql.intLiteral
import org.jetbrains.exposed.sql.javatime.timestampLiteral
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.sum
import org.jetbrains.exposed.sql.transactions.transaction
import java.time.LocalDate
import java.time.ZoneOffset.UTC
import java.util.UUID

private val totalLoyaltyPoints =
    coalesce(Services.loyaltyPoints.sum() plus Purchases.loyaltyPoints.sum(), Services.loyaltyPoints.sum(), Purchases.loyaltyPoints.sum(), intLiteral(0))

fun getTopClients(number: Int, since: LocalDate) = transaction {
    Clients.innerJoin(Appointments)
        .leftJoin(Services)
        .leftJoin(Purchases)
        .slice(Clients.id, totalLoyaltyPoints)
        .selectAll()
        .andWhere { Appointments.startTime greaterEq timestampLiteral(since.atStartOfDay().toInstant(UTC)) }
        .andWhere { Clients.banned eq booleanLiteral(false) }
        .groupBy(Clients.id)
        .orderBy(totalLoyaltyPoints, DESC)
        .limit(number)
        .map { TopClient(it[Clients.id].value, it[totalLoyaltyPoints] ?: 0) }
}

data class TopClient(val clientId: UUID, val loyaltyPoints: Int)

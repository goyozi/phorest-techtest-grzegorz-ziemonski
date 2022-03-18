import io.javalin.Javalin
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.transactions.transaction

fun connectToDatabase() {
    Database.connect("jdbc:postgresql:postgres", user = "postgres", password = "password")
}

val app: Javalin = Javalin.create().also { app ->
    app.post("/clients") { ctx ->
        transaction {
            connection.copyInto(Clients.tableName, ctx.bodyAsInputStream())
        }
    }
}

fun main() {
    connectToDatabase()
    app.start(7070)
}

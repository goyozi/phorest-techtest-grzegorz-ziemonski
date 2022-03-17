import io.javalin.Javalin
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.transactions.transaction

val app: Javalin = Javalin.create().also { app ->
    app.get("/") { ctx -> ctx.result("Hello World") }
}

fun main() {
    Database.connect("jdbc:postgresql:postgres", user = "postgres", password = "password")

    transaction {
        exec("select 'database connection works'") {
            it.next()
            println(it.getString(1))
        }
    }

    app.start(7070)
}

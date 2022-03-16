import io.javalin.Javalin

val app: Javalin = Javalin.create().also { app ->
    app.get("/") { ctx -> ctx.result("Hello World") }
}

fun main() {
    app.start(7070)
}

package com.interview

import io.javalin.http.Context
import java.time.LocalDate


fun getTopClients(ctx: Context) {
    val number = ctx.queryParamAsClass("number", Int::class.java)
        .check({ it > 0 }, "non-positive number parameter")
        .get()

    val since = ctx.queryParamAsClass("since", String::class.java)
        .get()
        .let { LocalDate.parse(it) }

    ctx.json(topClients(number, since))
}

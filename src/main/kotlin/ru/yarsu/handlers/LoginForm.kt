package ru.yarsu.handlers

import org.http4k.core.HttpHandler
import org.http4k.core.Response
import org.http4k.core.Status
import org.http4k.core.Status.Companion.BAD_REQUEST
import org.http4k.core.Status.Companion.OK
import org.http4k.core.body.form
import org.http4k.core.cookie.Cookie
import org.http4k.core.findSingle
import org.http4k.template.TemplateRenderer
import ru.yarsu.models.LoginFormVM
import ru.yarsu.users.AllUsers
import java.util.*

fun showLoginForm(renderer: TemplateRenderer): HttpHandler = {
    Response(OK).body(renderer(LoginFormVM("af", "sdg")))
}

fun loginUser(renderer: TemplateRenderer, users: AllUsers): HttpHandler = { request ->
    val form = request.form()
    println(form)
    val name = form.findSingle("type")
    println(name)
    Response(Status.FOUND).header("Location", "/animals")
    /*val form = request.form()
    println(form)
    val name = form.findSingle("name")
    val password = form.findSingle("password")?.ifEmpty { null }
    println(name)
    println(password)
    if (name.isNullOrEmpty() || password.isNullOrEmpty()) {
        Response(BAD_REQUEST).body(renderer(LoginFormVM(name, password)))
    } else if (users.getMapUsers()[name] == password) {
        println(users.getMapUsers()[name])
        JwtTools("seryhs", "sdgs", Date(2023, 10, 10)).createToken(name)?.let { jwtToken ->
            users.getCookies().add(Cookie("auth_token", jwtToken))

            Response(Status.FOUND).header("Location", "/animals")
        } ?: Response(BAD_REQUEST).body("Не удалось создать токен")
    } else {
        Response(BAD_REQUEST).body(renderer(LoginFormVM(name, password)))
    }*/
}

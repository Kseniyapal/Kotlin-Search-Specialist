package ru.yarsu.handlers

import org.http4k.core.HttpHandler
import org.http4k.core.Response
import org.http4k.core.Status
import org.http4k.core.body.form
import org.http4k.core.findSingle
import org.http4k.routing.path
import org.http4k.template.TemplateRenderer
import ru.yarsu.domain.Specialists
import ru.yarsu.models.DeleteFormVM
import ru.yarsu.models.NotFound404VM

fun showDeleteForm(renderer: TemplateRenderer, specialists: Specialists): HttpHandler = { request ->
    val id = request.path("id")!!.toInt()

    if (!checkId(id, specialists)) {
        Response(Status.OK).body(renderer(NotFound404VM(request.uri)))
    } else {
        Response(Status.OK).body(renderer(DeleteFormVM()))
    }
}

fun deleteSpecialists(renderer: TemplateRenderer, specialists: Specialists): HttpHandler = { request ->
    val id = request.path("id")!!.toInt()
    val form = request.form()
    val check = form.findSingle("ok").toString()
    if (check == "on") {
        specialists.getSpecialistById(id)?.let { specialist ->
            specialists.deleteSpecialist(specialist)
            Response(Status.FOUND).header("Location", "/specialists")
        } ?: Response(Status.OK).body(
            renderer(
                NotFound404VM(request.uri),
            ),
        )
    } else {
        Response(Status.OK).body(renderer(DeleteFormVM()))
    }
}

private fun checkId(id: Int, specialists: Specialists): Boolean {
    for (specialist in specialists.fetchAllSpecialist()) {
        if (specialist.index == id) {
            return true
        }
    }
    return false
}

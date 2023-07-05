package ru.yarsu.handlers

import org.http4k.core.HttpHandler
import org.http4k.core.Response
import org.http4k.core.Status
import org.http4k.core.body.form
import org.http4k.core.findSingle
import org.http4k.routing.path
import org.http4k.template.TemplateRenderer
import ru.yarsu.domain.Specialists
import ru.yarsu.models.CreateFormVM
import ru.yarsu.models.NotFound404VM
import ru.yarsu.validation.ValidatedFormField
import ru.yarsu.validation.validateInfo
import java.time.LocalDateTime

fun showEditForm(renderer: TemplateRenderer, specialists: Specialists): HttpHandler = { request ->
    val id = request.path("id")!!.toInt()
    specialists.getSpecialistById(id)?.let { specialist ->
        var stringEducation = ""
        for (item in specialist.education) {
            stringEducation += "$item,"
        }
        val listValues: List<String> = mutableListOf(
            specialist.service,
            specialist.name,
            specialist.description,
            specialist.nsp,
            stringEducation,
            specialist.phoneNumber,
        )
        val validatedForm = ValidatedFormField(
            listValues,
            mutableMapOf(),
        )
        Response(Status.OK).body(renderer(CreateFormVM(validatedForm, "Edit")))
    } ?: Response(Status.OK).body(
        renderer(
            NotFound404VM(request.uri),
        ),
    )
}

fun editSpecialists(renderer: TemplateRenderer, specialists: Specialists): HttpHandler = { request ->
    val form = request.form()
    val service = form.findSingle("service").toString()
    val name = form.findSingle("name").toString()
    val description = form.findSingle("description").toString()
    val nsp = form.findSingle("nsp").toString()
    val education = form.findSingle("education").toString()
    val phoneNumber = form.findSingle("phoneNumber").toString()
    val validate = validateInfo(
        listOf(
            service,
            name,
            description,
            nsp,
            education,
            phoneNumber,
        ),
    )

    if (validate.mapErrors.isNotEmpty()) {
        Response(Status.BAD_REQUEST).body(renderer(CreateFormVM(validate, "Edit")))
    } else {
        val id = request.path("id")!!.toInt()
        specialists.getSpecialistById(id)?.let { specialist ->
            specialist.service = service
            specialist.description = description
            specialist.name = name
            specialist.nsp = nsp
            specialist.education = education.split(",")
            specialist.phoneNumber = phoneNumber
            specialist.time = LocalDateTime.now()
            Response(Status.FOUND).header("Location", "/specialist/$id")
        } ?: Response(Status.OK).body(
            renderer(
                NotFound404VM(request.uri),
            ),
        )
    }
}

package ru.yarsu.handlers

import org.http4k.core.*
import org.http4k.core.body.form
import org.http4k.lens.webForm
import org.http4k.template.TemplateRenderer
import ru.yarsu.contexts
import ru.yarsu.domain.Specialist
import ru.yarsu.domain.Specialists
import ru.yarsu.models.CreateFormVM
import ru.yarsu.validation.ValidatedFormField
import ru.yarsu.validation.validateInfo
import java.time.LocalDateTime

fun showCreateForm(renderer: TemplateRenderer): HttpHandler = {
    val validatedForm = ValidatedFormField(listOf(), mutableMapOf())
    Response(Status.OK).body(renderer(CreateFormVM(validatedForm, "Create")))
}

fun createSpecialist(renderer: TemplateRenderer, specialists: Specialists): HttpHandler = { request ->
    val bodyString = request.bodyString()
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
        Response(Status.BAD_REQUEST).body(renderer(CreateFormVM(validate, "Create")))
    } else {
        val id = specialists.generateId()
        println(id)
        val specialist =
            Specialist(
                id,
                LocalDateTime.now(),
                service,
                name,
                description,
                nsp,
                education.split(","),
                phoneNumber,
            )

        specialists.add(specialist)
        Response(Status.FOUND).header("Location", "/specialist/$id")
    }
}

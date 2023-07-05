package ru.yarsu.models

import org.http4k.template.ViewModel
import ru.yarsu.validation.ValidatedFormField

class CreateFormVM(
    val validateInfo: ValidatedFormField,
    val toDo: String,
) : ViewModel

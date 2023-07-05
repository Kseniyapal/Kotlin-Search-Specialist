package ru.yarsu.models

import org.http4k.template.ViewModel
import ru.yarsu.validation.ValidatedFormField

class LoginFormVM(val name: String?, val password: String?) : ViewModel {
}
package ru.yarsu.models

import org.http4k.template.ViewModel
import ru.yarsu.domain.Specialist

data class SpecialistVM(val specialist: Specialist) : ViewModel

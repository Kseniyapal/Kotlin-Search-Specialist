package ru.yarsu.models

import org.http4k.template.ViewModel
import ru.yarsu.domain.Pagination
import ru.yarsu.domain.Specialist

class SpecialistsVM(
    val specialists: List<Specialist>,
    val paginator: Pagination,
    val serviceToFilter: String?,
    val nameToFilter: String?,
) : ViewModel

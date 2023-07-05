package ru.yarsu.validation

data class ValidatedFormField(
    val listValues: List<String>,
    val mapErrors: MutableMap<String, String>,
)

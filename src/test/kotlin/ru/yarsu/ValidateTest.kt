package ru.yarsu

import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.maps.shouldBeEmpty
import io.kotest.matchers.maps.shouldNotBeEmpty
import ru.yarsu.validation.validateInfo

class ValidateTest : FunSpec({
    test("Should validate ") {
        val service = "Услуга"
        val name = "Название"
        val description = "Описание"
        val nsp = "Имя Фамилия"
        val education = "Образование"
        val number = "+79999999999"
        val list = listOf(
            service,
            name,
            description,
            nsp,
            education,
            number,
        )
        validateInfo(list).mapErrors.shouldBeEmpty()
    }

    test("Should not validate ") {
        val service = "услуга"
        val name = "Наasasaзвание"
        val description = "!-[=исание"
        val nsp = "Имя"
        val education = "Образование fqd"
        val number = "+79"
        val list = listOf(
            service,
            name,
            description,
            nsp,
            education,
            number,
        )
        validateInfo(list).mapErrors.shouldNotBeEmpty()
    }
})

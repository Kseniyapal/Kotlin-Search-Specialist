package ru.yarsu.validation

import ru.yarsu.FIVE
import ru.yarsu.FOUR
import ru.yarsu.ONE
import ru.yarsu.THREE
import ru.yarsu.TWO
import ru.yarsu.ZERO

fun validateInfo(values: List<String>): ValidatedFormField {
    val validation = ValidatedFormField(values, mutableMapOf())
    validateService(values, validation)
    validateName(values, validation)
    validateDescription(values, validation)
    validateNSP(values, validation)
    validateEducation(values, validation)
    validateNumber(values, validation)
    return validation
}

private fun validateService(values: List<String>, validation: ValidatedFormField) {
    if (!(
            values[ZERO].isNotEmpty() && values[ZERO].matches("^[а-яА-Я-ёЁ\\s]*$".toRegex()) &&
                values[ZERO][ZERO].isUpperCase()
            )
    ) {
        validation.mapErrors["service"] = "Введите корректное значение! " +
            "(С заглавной буквы, только кирилица, непустое)"
    }
}

private fun validateName(values: List<String>, validation: ValidatedFormField) {
    if (!(
            values[ONE].isNotEmpty() && values[ONE].matches("^[а-яА-Я-ёЁ\\s]*$".toRegex()) &&
                values[ONE][ZERO].isUpperCase()
            )
    ) {
        validation.mapErrors["name"] = "Введите корректное значение! " +
            "(С заглавной буквы, только кирилица, непустое)"
    }
}

private fun validateDescription(values: List<String>, validation: ValidatedFormField) {
    if (!(
            values[TWO].isNotEmpty() && values[TWO].matches("^[?!,.:;а-яА-Я-ёЁ–0-9\\s]*\$".toRegex()) &&
                values[TWO][ZERO].isUpperCase()
            )
    ) {
        validation.mapErrors["description"] = "Введите корректное значение! " +
            "(С заглавной буквы, только кирилица, непустое)"
    }
}

private fun validateNSP(values: List<String>, validation: ValidatedFormField) {
    if (!(
            values[THREE].isNotEmpty() &&
                values[THREE].matches(
                    (
                        "^[а-яА-ЯёЁa-zA-Z]+ [а-яА-ЯёЁa-zA-Z]+ " +
                            "?[а-яА-ЯёЁa-zA-Z]+$"
                        ).toRegex(),
                ) &&
                values[THREE][ZERO].isUpperCase()
            )
    ) {
        validation.mapErrors["nsp"] = "Введите корректное значение! " +
            "(С заглавной буквы, только кирилица, непустое, имя и фамилия обязательны)"
    }
}

private fun validateEducation(values: List<String>, validation: ValidatedFormField) {
    if (!(
            values[FOUR].isNotEmpty() && values[FOUR].matches("^[,а-яА-Я-ёЁ\\s]*\$".toRegex()) &&
                values[FOUR][ZERO].isUpperCase()
            )
    ) {
        validation.mapErrors["education"] = "Введите корректное значение! " +
            "(С заглавной буквы, только кирилица, непустое, виды образования разделяйте только запятой!)"
    }
}

private fun validateNumber(values: List<String>, validation: ValidatedFormField) {
    if (!(
            values[FIVE].isNotEmpty() &&
                values[FIVE].matches(
                    (
                        "^(\\+)?((\\d{2,3}) ?\\d|\\d)" +
                            "(([ -]?\\d)|( ?(\\d{2,3}) ?)){5,12}\\d\$"
                        ).toRegex(),
                )
            )
    ) {
        validation.mapErrors["phoneNumber"] = "Введите корректное значение! " +
            "(Непустое, номер телефона)"
    }
}

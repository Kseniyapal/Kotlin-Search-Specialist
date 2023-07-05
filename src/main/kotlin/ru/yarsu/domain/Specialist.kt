package ru.yarsu.domain

import java.time.LocalDateTime

class Specialist(
    val index: Int,
    var time: LocalDateTime,
    var service: String,
    var name: String,
    var description: String,
    var nsp: String,
    var education: List<String>,
    var phoneNumber: String,
) {
    fun dateOf(): String {
        return time.toString().split("T")[0]
    }

    fun timeOf(): String {
        return time.toString().split("T")[1]
    }
}

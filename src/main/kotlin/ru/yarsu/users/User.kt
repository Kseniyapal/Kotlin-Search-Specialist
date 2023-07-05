package ru.yarsu.users

import java.time.LocalDateTime

data class User(val id: String, val name: String, val password: String, val date: LocalDateTime, val role: String)

package ru.yarsu.users

import org.http4k.core.cookie.Cookie

class AllUsers(var users: MutableList<User>, val salt: String) {
    private val cookies = mutableListOf<Cookie>()
    fun getMapUsers(): MutableMap<String, String> {
        val mapUsers = mutableMapOf<String, String>()
        for (user in users) {
            mapUsers[user.name] = user.password
        }
        return mapUsers
    }

    fun getCookies(): MutableList<Cookie> {
        return cookies
    }

    fun add(user: User) {
        users.add(user)
    }
}

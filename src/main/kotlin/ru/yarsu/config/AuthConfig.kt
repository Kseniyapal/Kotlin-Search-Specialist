package ru.yarsu.config

import org.http4k.cloudnative.env.Environment
import org.http4k.cloudnative.env.EnvironmentKey
import org.http4k.lens.nonEmptyString

class AuthConfig(val salt: String, val token: String) {
    companion object {
        private val saltLens = EnvironmentKey.nonEmptyString().required("auth.salt")
        private val tokenLens = EnvironmentKey.nonEmptyString().required("token.salt")
        fun createAuthConfig(environment: Environment): AuthConfig {
            return AuthConfig(saltLens(environment), tokenLens(environment))
        }
    }
}

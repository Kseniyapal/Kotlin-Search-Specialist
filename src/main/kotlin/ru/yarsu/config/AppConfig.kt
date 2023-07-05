package ru.yarsu.config

import org.http4k.cloudnative.env.Environment
import ru.yarsu.config.WebConfig.Companion.defaultEnv

class AppConfig(
    val webConfig: WebConfig,
    val authConfig: AuthConfig,
) {
    companion object {
        private val appEnv = Environment.ENV overrides
            Environment.JVM_PROPERTIES overrides
            Environment.fromResource("app.properties") overrides
            defaultEnv

        fun readConfiguration(environment: Environment = appEnv) = AppConfig(
            WebConfig.createWebConfig(environment),
            AuthConfig.createAuthConfig(environment),
        )
    }
}

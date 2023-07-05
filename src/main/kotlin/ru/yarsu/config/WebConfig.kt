package ru.yarsu.config

import org.http4k.cloudnative.env.Environment
import org.http4k.cloudnative.env.EnvironmentKey
import org.http4k.lens.int
import org.http4k.lens.nonEmptyString

class WebConfig(val port: Int) {
    companion object {
        private val portLens = EnvironmentKey.int().required("web.port")
        val defaultEnv = Environment.defaults(
            portLens of 1515,
        )
        fun createWebConfig(environment: Environment): WebConfig {
            return WebConfig(portLens(environment))
        }
    }
}

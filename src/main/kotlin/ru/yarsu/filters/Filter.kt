package ru.yarsu.filters

import org.http4k.core.Filter
import org.http4k.core.HttpHandler
import org.http4k.core.Status
import org.http4k.template.PebbleTemplates
import ru.yarsu.models.NotFound404VM

val renderer = PebbleTemplates().HotReload("src/main/resources")

val errorFilter = Filter { handler: HttpHandler ->
    { request ->
        val response = handler(request)
        if (response.status == Status.NOT_FOUND && response.body.length == 0L) {
            response.body(renderer(NotFound404VM(request.uri)))
        } else {
            response
        }
    }
}

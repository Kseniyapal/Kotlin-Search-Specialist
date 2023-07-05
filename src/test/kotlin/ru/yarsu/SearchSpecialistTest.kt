package ru.yarsu

import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.should
import io.kotest.matchers.shouldBe
import org.http4k.core.HttpHandler
import org.http4k.core.Method
import org.http4k.core.Request
import org.http4k.core.Status
import org.http4k.core.UriTemplate
import org.http4k.core.then
import org.http4k.kotest.haveStatus
import org.http4k.routing.RoutedRequest
import org.http4k.template.PebbleTemplates
import ru.yarsu.domain.Specialist
import ru.yarsu.domain.Specialists
import ru.yarsu.filters.errorFilter
import java.time.LocalDateTime

class SearchSpecialistTest : FunSpec({

    val renderer = PebbleTemplates().HotReload("src/main/resources")
    val specialist0 = Specialist(
        0,
        LocalDateTime.parse("2019-02-23T14:14:14"),
        "Доставка",
        "Delivery",
        "Быстрая доставка, в черте города, доставка еды, доставка мелкогабаритных товаров".trimMargin(),
        "Петров Илья Владимирович",
        listOf("Основное общее", "Среднее профессиональное"),
        "+79943893535",
    )
    val specialist1 = Specialist(
        1,
        LocalDateTime.parse("2012-02-18T12:15:19"),
        "Доставка",
        "Delivery",
        "Уборка больших площадей, влажная уборка, Сухая уборка".trimMargin(),
        "Иванов Иван Валерьевич",
        listOf("Среднее общее образование", "Высшее"),
        "+7829844874367",
    )
    val specialist2 = Specialist(
        2,
        LocalDateTime.parse("2022-12-08T12:14:12"),
        "Продажа антиквариата",
        "Антиквариат",
        "Продажа советской техники, продажа старых игрушек, продажа старых значков".trimMargin(),
        "Носков Егор Викторович",
        listOf("Среднее общее образование", "Среднее профессиональное", "Высшее"),
        "+7829897473797",
    )
    val specialist3 = Specialist(
        3,
        LocalDateTime.parse("2015-01-19T04:04:04"),
        "Клининг",
        "Чистота",
        "Уборка маленьких комнат, влажная уборка, сухая уборка".trimMargin(),
        "Котов Владимир Александрович",
        listOf("Общее образование", "Среднее профессиональное", "Высшее"),
        "+7829844874367",
    )
    val list = mutableListOf(
        specialist0,
        specialist1,
        specialist2,
        specialist3,
    )
    val spec = Specialists(list)
    test("Should render start page") {
        val handler = index(renderer)
        val request = Request(Method.GET, "/")
        val result = handler(request)
        result.should(haveStatus(Status.OK))
    }

    test("Should render list specialists") {
        val handler = allSpecialists(renderer, spec)
        val request = Request(Method.GET, "/specialists")
        val result = handler(request)
        result.should(haveStatus(Status.OK))
    }

    test("Should render specialist by id") {
        val handler = getSpecialist(renderer, spec)
        val request = Request(Method.GET, "/specialist/3")
        val routedRequest = RoutedRequest(request, UriTemplate.from("/specialist/{index}"))
        val result = handler(routedRequest)
        result.should(haveStatus(Status.OK))
    }

    test("Should filter error pages") {
        val request = Request(Method.GET, "/")
        val app: HttpHandler = errorFilter.then(router(renderer, spec, users, key))
        val response = app(request)
        response.status.shouldBe(Status.OK)
    }

    test("Should filter error pages with status not found") {
        val request = Request(Method.GET, "/nonexist")
        val app: HttpHandler = errorFilter.then(router(renderer, spec, users, key))
        val response = app(request)
        response.status.shouldBe(Status.NOT_FOUND)
    }
})

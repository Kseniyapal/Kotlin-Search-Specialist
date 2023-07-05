package ru.yarsu

import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.collections.shouldNotContain
import io.kotest.matchers.should
import io.kotest.matchers.shouldBe
import org.http4k.core.Method
import org.http4k.core.Request
import org.http4k.core.Status
import org.http4k.core.UriTemplate
import org.http4k.core.body.form
import org.http4k.kotest.haveStatus
import org.http4k.routing.RoutedRequest
import org.http4k.template.PebbleTemplates
import ru.yarsu.domain.Specialist
import ru.yarsu.domain.Specialists
import ru.yarsu.handlers.deleteSpecialists
import ru.yarsu.handlers.showDeleteForm
import java.time.LocalDateTime

class DeleteTest : FunSpec({
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

    test("Should show delete form") {
        val handler = showDeleteForm(renderer, Specialists(list))
        val request = Request(Method.GET, "/specialist/0/delete")
        val routedRequest = RoutedRequest(request, UriTemplate.from("/specialist/{id}/delete"))
        val result = handler(routedRequest)
        result.should(haveStatus(Status.OK))
    }
    test("Should delete specialist") {
        val specialists = Specialists(list)
        val handler = deleteSpecialists(renderer, specialists)
        val request = Request(Method.POST, "/specialist/0/delete")
        val routedRequest = RoutedRequest(request, UriTemplate.from("/specialist/{id}/delete"))
        val requestWithFormData = routedRequest.form("ok", "on")
        handler(requestWithFormData)
        specialists.fetchAllSpecialist().size.shouldBe(3)
        specialists.fetchAllSpecialist().shouldNotContain(specialist0)
    }

    test("Should not delete specialist") {
        val specialists = Specialists(list)
        val handler = deleteSpecialists(renderer, specialists)
        val request = Request(Method.POST, "/specialist/0/delete")
        val routedRequest = RoutedRequest(request, UriTemplate.from("/specialist/{id}/delete"))
        handler(routedRequest)
        specialists.fetchAllSpecialist().size.shouldBe(4)
    }
})

package ru.yarsu

import io.kotest.core.spec.style.FunSpec
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
import ru.yarsu.handlers.createSpecialist
import ru.yarsu.handlers.showCreateForm
import java.time.LocalDateTime

class CreateTest : FunSpec({
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

    test("Should show create form") {
        val handler = showCreateForm(renderer)
        val request = Request(Method.GET, "/specialist/create")
        val result = handler(request)
        result.should(haveStatus(Status.OK))
    }

    test("Should not create new specialist") {
        val handler = createSpecialist(renderer, Specialists(list))
        val request = Request(Method.POST, "/specialist/create")
        val result = handler(request)
        result.should(haveStatus(Status.BAD_REQUEST))
    }

    test("Should create new specialist") {
        val spec = Specialists(list)
        val handler = createSpecialist(renderer, spec)
        val request = Request(Method.POST, "/specialist/create")
        val routedRequest = RoutedRequest(request, UriTemplate.from("/specialist/create"))
        val requestWithFormData = routedRequest
            .form("service", "Услуга")
            .form("name", "Название")
            .form("description", "Описание")
            .form("nsp", "ФИО ФИО")
            .form("education", "Образование")
            .form("phoneNumber", "+79999999999")
        val result = handler(requestWithFormData)
        result.should(haveStatus(Status.FOUND))
        spec.fetchAllSpecialist().size.shouldBe(5)
    }
})

package ru.yarsu

import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.collections.shouldContain
import io.kotest.matchers.collections.shouldContainOnly
import io.kotest.matchers.collections.shouldNotContain
import io.kotest.matchers.shouldBe
import ru.yarsu.domain.Specialist
import ru.yarsu.domain.Specialists
import java.time.LocalDateTime

class SpecialistsTest : FunSpec({
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

    test("Should get the list of specialists") {
        val specialists = Specialists(list)
        specialists.fetchAllSpecialist().shouldBe(listOf(specialist1, specialist3, specialist0, specialist2))
    }
    test("Should add specialists to specialists") {
        val specialists = Specialists(list)
        val specialist = Specialist(
            4,
            LocalDateTime.now(),
            "Service",
            "Name",
            "Description",
            "NSP",
            listOf("Education1", "Education2"),
            "+79999999999",
        )
        specialists.add(specialist)
        specialists.fetchAllSpecialist().shouldContain(specialist)
    }

    test("Should fetch specialists by service") {
        val specialists = Specialists(list)
        val specialistsByService = specialists.fetchByService("Доставка")
        specialistsByService.shouldBe(
            listOf(
                specialist0,
                specialist1,
            ),
        )
    }

    test("Should get amount of page") {
        val specialists = Specialists(list)
        specialists.pageAmount().shouldBe(1)
    }

    test("Should get specialists by page") {
        val specialists = Specialists(list)
        specialists.specialistsByPageNumber(1).shouldBe(list)
    }

    test("Should get unique services") {
        val specialists = Specialists(list)
        specialists.uniqueServices().shouldBe(listOf("Доставка", "Продажа антиквариата", "Клининг"))
    }

    test("Should sort by ascendingTime") {
        val specialists = Specialists(list)
        val sortedSpecialists = specialists.sortByParametr("ascendingTime")
        sortedSpecialists.shouldBe(
            listOf(
                specialist1,
                specialist3,
                specialist0,
                specialist2,
            ),
        )
    }

    test("Should sort by descendingTime") {
        val specialists = Specialists(list)
        val sortedSpecialists = specialists.sortByParametr("descendingTime")
        sortedSpecialists.shouldBe(
            listOf(
                specialist2,
                specialist0,
                specialist3,
                specialist1,
            ),
        )
    }

    test("Should sort by ascendingService") {
        val specialists = Specialists(list)
        val sortedSpecialists = specialists.sortByParametr("ascendingService")
        sortedSpecialists.shouldBe(
            listOf(
                specialist0,
                specialist1,
                specialist3,
                specialist2,
            ),
        )
    }

    test("Should sort by descendingService") {
        val specialists = Specialists(list)
        val sortedSpecialists = specialists.sortByParametr("descendingService")
        sortedSpecialists.shouldBe(
            listOf(
                specialist2,
                specialist3,
                specialist0,
                specialist1,
            ),
        )
    }

    test("Should sort by ascendingIndex") {
        val specialists = Specialists(list)
        val sortedSpecialists = specialists.sortByParametr("ascendingIndex")
        sortedSpecialists.shouldBe(
            listOf(
                specialist0,
                specialist1,
                specialist2,
                specialist3,
            ),
        )
    }

    test("Should sort by descendingIndex") {
        val specialists = Specialists(list)
        val sortedSpecialists = specialists.sortByParametr("descendingIndex")
        sortedSpecialists.shouldBe(
            listOf(
                specialist3,
                specialist2,
                specialist1,
                specialist0,
            ),
        )
    }

    test("Should generate id") {
        val specialists = Specialists(list)
        specialists.generateId().shouldBe(4)
    }

    test("Should delete specialist by ID") {
        val specialists = Specialists(list)
        specialists.deleteSpecialist(specialist0)
        specialists.fetchAllSpecialist().shouldNotContain(specialist0)
    }

    test("Should get specialist by ID") {
        val specialists = Specialists(list)
        val sp = specialists.getSpecialistById(2)
        sp.shouldBe(specialist2)
    }

    test("Should filter by service") {
        val specialists = Specialists(list)
        val filters = specialists.specialistFilter("Доставка", null)
        filters.shouldContainOnly(specialist0, specialist1)
    }

    test("Should filter by name") {
        val specialists = Specialists(list)
        val filters = specialists.specialistFilter(null, "Delivery")
        filters.shouldContainOnly(specialist0, specialist1)
    }

    test("Should filter by service and name") {
        val specialists = Specialists(list)
        val filters = specialists.specialistFilter("Доставка", "Delivery")
        filters.shouldContainOnly(specialist0, specialist1)
    }

    test("Should filter by null") {
        val specialists = Specialists(list)
        val filters = specialists.specialistFilter(null, null)
        filters.shouldContainOnly(specialist0, specialist1, specialist2, specialist3)
    }
})

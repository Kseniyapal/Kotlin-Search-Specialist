package ru.yarsu

import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.booleans.shouldBeFalse
import io.kotest.matchers.booleans.shouldBeTrue
import io.kotest.matchers.collections.shouldContain
import io.kotest.matchers.shouldBe
import org.http4k.core.Uri
import org.http4k.core.query
import ru.yarsu.domain.InfoPage
import ru.yarsu.domain.Pagination

class PaginatorTest : FunSpec({
    val uri = Uri.of("http://localhost:9000/specialists")
    test("Should check possibility to go back") {
        val pagination = Pagination(uri, 1, 5)
        pagination.possibilityToGoBack().shouldBeFalse()
    }

    test("Should check possibility to go forward") {
        val pagination = Pagination(uri, 1, 5)
        pagination.possibilityToGoForward().shouldBeTrue()
    }

    test("Should check possibility to go next") {
        val pagination = Pagination(uri, 1, 5)
        pagination.possibilityToGoForward().shouldBeTrue()
    }

    test("Should get current page") {
        val pagination = Pagination(uri, 3, 5)
        pagination.getNowPage().shouldBe(3)
    }

    test("Should get previous page") {
        val pagination = Pagination(uri, 3, 5)
        pagination.previousPage().query.shouldBe("page=2")
    }

    test("Should get forward page") {
        val pagination = Pagination(uri, 3, 5)
        pagination.nextPage().query.shouldBe("page=4")
    }

    test("Should get list previous pages") {
        val pagination = Pagination(uri, 3, 5)
        pagination.previousPages().shouldContain(InfoPage(1, uri.query("page", "1")))
        pagination.previousPages().shouldContain(InfoPage(2, uri.query("page", "2")))
    }

    test("Should get list next pages") {
        val pagination = Pagination(uri, 3, 5)
        pagination.nextPages().shouldContain(InfoPage(4, uri.query("page", "4")))
        pagination.nextPages().shouldContain(InfoPage(5, uri.query("page", "5")))
    }
})

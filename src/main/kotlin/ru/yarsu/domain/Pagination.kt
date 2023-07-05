package ru.yarsu.domain

import org.http4k.core.Uri
import org.http4k.core.query
import org.http4k.core.removeQuery

class Pagination(
    uri: Uri,
    val number: Int,
    val amount: Int,
) {
    val uri = uri.removeQuery("page")

    fun possibilityToGoBack(): Boolean {
        return number > 1
    }

    fun possibilityToGoForward(): Boolean {
        return number < amount
    }

    fun getNowPage(): Int {
        return number
    }
    fun previousPage(): Uri = uri.query("page", value = (number - 1).toString())
    fun nextPage(): Uri = uri.query("page", value = (number + 1).toString())

    fun previousPages(): List<InfoPage> {
        val listPreviousPage = mutableListOf<InfoPage>()
        for (i in 1 until number) {
            listPreviousPage.add(InfoPage(i, uri.query("page", (i).toString())))
            uri.removeQuery("page")
        }
        return listPreviousPage
    }

    fun nextPages(): List<InfoPage> {
        val listNextPage = mutableListOf<InfoPage>()
        for (i in number + 1..amount) {
            listNextPage.add(InfoPage(i, uri.query("page", (i).toString())))
        }
        return listNextPage
    }
}

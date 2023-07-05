package ru.yarsu

import org.http4k.core.*
import org.http4k.core.Method.GET
import org.http4k.core.Status.Companion.OK
import org.http4k.core.body.form
import org.http4k.core.cookie.Cookie
import org.http4k.core.cookie.cookie
import org.http4k.filter.ServerFilters
import org.http4k.lens.BiDiLens
import org.http4k.lens.RequestContextKey
import org.http4k.lens.RequestContextLens
import org.http4k.routing.*
import org.http4k.server.Netty
import org.http4k.server.asServer
import org.http4k.template.PebbleTemplates
import org.http4k.template.TemplateRenderer
import ru.yarsu.config.AppConfig
import ru.yarsu.domain.Pagination
import ru.yarsu.domain.Specialists
import ru.yarsu.filters.errorFilter
import ru.yarsu.handlers.*
import ru.yarsu.models.*
import ru.yarsu.storage.readCsv
import ru.yarsu.users.AllUsers
import ru.yarsu.users.User
import java.util.*

const val PORT = 9000
const val ZERO = 0
const val ONE = 1
const val TWO = 2
const val THREE = 3
const val FOUR = 4
const val FIVE = 5
const val SIX = 6
const val POSTS = 20

fun router(
    renderer: TemplateRenderer,
    specialists: Specialists,
    users: AllUsers,
    key: BiDiLens<Request, UserContext?>,
): HttpHandler = routes(
    "/" bind GET to index(renderer),
    "/specialists" bind GET to allSpecialists(renderer, specialists),
    "/menu" bind GET to menu(renderer, specialists),
    "/specialist/create" bind GET to showCreateForm(renderer),
    "/specialist/create" bind Method.POST to createSpecialist(renderer, specialists),
    "/specialist/{id}/edit" bind GET to showEditForm(renderer, specialists),
    "/specialist/{id}/edit" bind Method.POST to editSpecialists(renderer, specialists),
    "/specialist/{id}/delete" bind GET to showDeleteForm(renderer, specialists),
    "/specialist/{id}/delete" bind Method.POST to deleteSpecialists(renderer, specialists),
    "/specialists/{service}" bind GET to specialistService(renderer, specialists),
    "/specialist/{index}" bind GET to getSpecialist(renderer, specialists),
    static(ResourceLoader.Classpath("/ru/yarsu/public/")),
)

fun getSpecialist(renderer: TemplateRenderer, listSpecialist: Specialists): HttpHandler = { request ->
    val index = request.path("index")?.toIntOrNull() ?: -1
    listSpecialist.getSpecialistById(index)?.let { specialist ->
        Response(OK).body(renderer(SpecialistVM(specialist)))
    } ?: Response(OK).body(
        renderer(
            NotFound404VM(request.uri),
        ),
    )
}

fun index(renderer: TemplateRenderer): HttpHandler = {
    Response(OK).body(renderer(IndexVM()))
}

fun allSpecialists(renderer: TemplateRenderer, specialists: Specialists): HttpHandler = {
    val pageNum = it.uri.queries().findSingle("page")?.toIntOrNull() ?: 1
    val sort = it.uri.queries().findSingle("sort").toString()
    val serviceFilter = it.uri.queries().findSingle("service")?.ifEmpty { null }
    val nameFilter = it.uri.queries().findSingle("name")?.ifEmpty { null }
    val sortedSpecialists = Specialists(specialists.sortByParametr(sort))
    val filteredSpecialists = Specialists(sortedSpecialists.specialistFilter(serviceFilter, nameFilter))
    val specialistsByPageNumber = filteredSpecialists.specialistsByPageNumber(pageNum)
    val pagination = Pagination(it.uri, pageNum, filteredSpecialists.pageAmount())
    Response(OK).body(renderer(SpecialistsVM(specialistsByPageNumber, pagination, serviceFilter, nameFilter)))
}

fun menu(renderer: TemplateRenderer, listMenu: Specialists): HttpHandler = {
    Response(OK).body(renderer(MenuVM(listMenu.uniqueServices())))
}

fun specialistService(renderer: TemplateRenderer, listSpecialist: Specialists): HttpHandler = { request ->
    val service: String = request.path("service").toString()
    val sort = request.uri.queries().findSingle("sort").toString()
    val nameFilter = request.uri.queries().findSingle("name")?.ifEmpty { null }
    val sortedSpecialists = Specialists(listSpecialist.sortByParametr(sort))
    val filterSpecialist = Specialists(sortedSpecialists.specialistFilter(null, nameFilter))
    val pageNum = request.uri.queries().findSingle("page")?.toIntOrNull() ?: 1
    val specialists = filterSpecialist.fetchByService(service)
    val specialistsByPageNumber = Specialists(specialists).specialistsByPageNumber(pageNum)
    val pagination = Pagination(request.uri, pageNum, Specialists(specialists).pageAmount())
    Response(OK).body(renderer(SpecialistsVM(specialistsByPageNumber, pagination, null, nameFilter)))
}

data class UserContext(val auth_token: String)

val renderer = PebbleTemplates().HotReload("src/main/resources")
val contexts = RequestContexts()
val key = RequestContextKey.optional<UserContext>(contexts)
val listUser = mutableListOf<User>()
fun getUserById(id: String): User? {
    return listUser.find { user -> user.id == id }
}

/*fun getUserFromJWT(token: String): User{
    return listUser.
}*/
fun authFilter(key: RequestContextLens<UserContext?>) = Filter { next ->
    { request: Request ->
        println(request.form())
        request.cookie("auth_token")?.let { cookie: Cookie ->
            val jwtToken = cookie.keyValueCookieString()
            JwtTools("sdgergkpotto", "poop", Date(2024, 2, 2)).checkToken(jwtToken)?.let { id ->
                getUserById(id)?.let { user ->
                    next(request.with(key of UserContext(cookie.value)))
                } ?: next(request.body(renderer(NotFound404VM(request.uri))))
            } ?: next(request.body(renderer(NotFound404VM(request.uri))))
        } ?: next(request.body(renderer(NotFound404VM(request.uri))))
    }
}

/*fun addStateFilter(key: RequestContextLens<UserContext?>) = Filter { next ->
    { request ->
        next(request.with(key of UserContext()))
    }
}*/

fun main() {
    val appConfig = AppConfig.readConfiguration()
    val specialists = readCsv()
    /*val app = router(renderer, specialists)
    val server = Filter.NoOp
        .then(errorFilter)
        .then(app)
        .asServer(Netty(appConfig.webConfig.port)).start()*/
    val users = AllUsers(mutableListOf(), appConfig.authConfig.salt)
    val app = ServerFilters.InitialiseRequestContext(contexts)
        .then(authFilter(key))
        //.then(addStateFilter(key))
        .then(errorFilter)
        .then(router(renderer, specialists, users, key))
        .asServer(Netty(appConfig.webConfig.port)).start()
    println("Server started on http://localhost:" + app.port())
}

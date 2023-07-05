package ru.yarsu.domain

import ru.yarsu.ONE
import ru.yarsu.POSTS
import ru.yarsu.ZERO
import kotlin.math.min

class Specialists(listSpecialists: MutableList<Specialist>) {
    private var specialists = listSpecialists.toMutableList()
    private var freeId = mutableSetOf<Int>()

    fun add(specialist: Specialist) {
        specialists.add(specialist)
    }

    fun fetchAllSpecialist(): MutableList<Specialist> {
        return specialists.sortedBy { it.time }.toMutableList()
    }

    fun fetchByService(service: String): MutableList<Specialist> {
        val listSpecialist: MutableList<Specialist> = mutableListOf()
        for (i in specialists.indices) {
            if (specialists[i].service == service) {
                listSpecialist.add(specialists.get(i))
            }
        }
        return listSpecialist
    }

    fun pageAmount(): Int {
        if (specialists.size % POSTS == ZERO) {
            return specialists.size / POSTS
        }
        return specialists.size / POSTS + ONE
    }

    fun specialistsByPageNumber(number: Int): MutableList<Specialist> {
        val maxNum = min(POSTS * number, specialists.size)
        val listPageTriangles = specialists.subList(POSTS * (number - ONE), maxNum)
        return listPageTriangles
    }

    fun uniqueServices(): List<String> {
        val list: MutableList<String> = mutableListOf()
        for (i in specialists.indices) {
            if (specialists[i].service !in list) {
                list.add(specialists[i].service)
            }
        }
        return list
    }

    fun sortByParametr(parametr: String): MutableList<Specialist> {
        if (parametr == "ascendingTime") {
            specialists = specialists.sortedBy { it.time }.toMutableList()
        }
        if (parametr == "descendingTime") {
            specialists = specialists.sortedByDescending { it.time }.toMutableList()
        }
        if (parametr == "ascendingService") {
            specialists = specialists.sortedBy { it.service }.toMutableList()
        }
        if (parametr == "descendingService") {
            specialists = (specialists.sortedByDescending { it.service }.toMutableList())
        }
        if (parametr == "ascendingIndex") {
            specialists = specialists.sortedBy { it.index }.toMutableList()
        }
        if (parametr == "descendingIndex") {
            specialists = specialists.sortedByDescending { it.index }.toMutableList()
        }
        return specialists
    }

    fun generateId(): Int {
        val id = freeId.firstOrNull() ?: specialists.size
        freeId.remove(id)
        return id
    }

    fun getSpecialistById(id: Int): Specialist? {
        return specialists.find { specialist -> specialist.index == id }
    }

    fun deleteSpecialist(specialist: Specialist) {
        freeId.add(specialist.index)
        specialists = specialists.toMutableList()
        specialists.remove(specialist)
    }

    fun specialistFilter(serviceToFilter: String?, nameToFilter: String?): MutableList<Specialist> {
        val serviceFiltered = serviceToFilter?.let {
            specialists.filter { specialist ->
                specialist.service == serviceToFilter
            }
        } ?: specialists
        return (
            nameToFilter?.let {
                serviceFiltered.filter { specialist -> specialist.name == nameToFilter }
            } ?: serviceFiltered
            ) as MutableList<Specialist>
    }
}

package ru.yarsu.storage

import org.apache.commons.csv.CSVFormat
import org.apache.commons.csv.CSVParser
import ru.yarsu.FIVE
import ru.yarsu.FOUR
import ru.yarsu.ONE
import ru.yarsu.SIX
import ru.yarsu.THREE
import ru.yarsu.TWO
import ru.yarsu.ZERO
import ru.yarsu.domain.Specialist
import ru.yarsu.domain.Specialists
import java.nio.file.Files
import java.nio.file.Paths
import java.time.LocalDateTime

fun readCsv(): Specialists {
    val filePath = "src/main/resources/ru/yarsu/info.csv"
    val reader = Files.newBufferedReader(Paths.get(filePath))
    val csvParser = CSVParser(reader, CSVFormat.DEFAULT)
    val specialists = Specialists(mutableListOf())
    for (csvRecord in csvParser) {
        val specialist = Specialist(
            specialists.generateId(),
            LocalDateTime.parse(csvRecord.get(ONE)),
            csvRecord.get(ZERO),
            csvRecord.get(TWO),
            csvRecord.get(THREE),
            csvRecord.get(FOUR),
            csvRecord.get(FIVE).split(","),
            csvRecord.get(SIX),
        )
        specialists.add(specialist)
    }
    return specialists
}

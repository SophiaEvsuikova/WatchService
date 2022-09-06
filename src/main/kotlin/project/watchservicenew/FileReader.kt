package project.watchservicenew

import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import mu.KotlinLogging
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component

import java.io.BufferedReader
import java.nio.file.Paths

@Component
class FileReader(@Value("\${app.filepath}") val filepath: String) {

    @Autowired
    lateinit var dataBaseService: DataBaseService

    private val logger = KotlinLogging.logger {}
    fun read(fileName: String): List<PersonData> {

        val file = Paths.get(filepath, fileName).toFile()
        var obj = listOf<PersonData>()

        if (file.exists()) {
            val bufferedReader: BufferedReader = file.bufferedReader()
            val inputString = bufferedReader.use { it.readText() }.trim()

            if (inputString.startsWith("[") and inputString.endsWith("]")) {
                obj = Json.decodeFromString(inputString)
                logger.info { "Get data $obj"}
            } else {
                obj = listOf(Json.decodeFromString(inputString))
                logger.info { "Get data $obj"}
            }
        }
        return obj
    }

    fun getPerson(peopleList: List<PersonData>){

        for (people in peopleList){
            if (dataBaseService.findPerson(people.name, people.lastname) > 0) {
                logger.info { "Ignore Person ${people.name} ${people.lastname}" }
            } else {
                dataBaseService.insert(people.name, people.lastname)
                logger.info { "Inserted Person ${people.name} ${people.lastname}" }
            }
        }
    }
}
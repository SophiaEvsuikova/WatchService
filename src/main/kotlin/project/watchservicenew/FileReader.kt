package project.watchservicenew

import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import mu.KotlinLogging
import org.springframework.beans.factory.annotation.Autowired
import java.io.BufferedReader
import java.nio.file.Paths


class FileReader {

    @Autowired
    var dataBaseService = DataBaseService()

    private val logger = KotlinLogging.logger {}
    fun read(fileName: String): List<PersonData> {

        val filePath = "D:\\Repositories\\watchServiceNew\\src\\main\\kotlin\\project\\assets"
        val file = Paths.get(filePath, fileName).toFile()
        var obj = listOf<PersonData>()

        if (file.exists()) {
            val bufferedReader: BufferedReader = file.bufferedReader()
            val inputString = bufferedReader.use { it.readText() }.trim()

            if (inputString.startsWith("[") and inputString.endsWith("]")) {
                obj = Json.decodeFromString(inputString)
                print(obj)
            } else {
                obj = listOf(Json.decodeFromString(inputString))
                print(obj)
            }
        }
        return obj
    }

    fun getPerson(peopleList: List<PersonData>){

        //val dataBaseService = DataBaseService()
        for (people in peopleList){
            if (dataBaseService.findPerson(people.name, people.lastname) > 0) {
                logger.info { "Ignore Person ${people.name} ${people.lastname}" }
            } else {
                dataBaseService.insert(people.name, people.lastname)
            }
        }
    }
}
package com.watchservice

import kotlinx.serialization.Serializable
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import java.io.BufferedReader
import java.nio.file.Paths

@Serializable
data class PersonData(val name: String, val lastname: String)

class FileReader {
    fun read(fileName: String): List<PersonData> {

        // TODO пути следует выносить в .properties файл
        val filePath = "D:\\Repositories\\WatchService\\src\\main\\kotlin\\assets"
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

        val dataBaseService = DataBaseService()

        for (people in peopleList){
            if (dataBaseService.findPerson(people.name, people.lastname) > 0) {
                // TODO в целях логгирования лучше использовать соответствующие библиотеки
                println("ignore Person ${people.name} ${people.lastname}")
            } else {
                dataBaseService.insert(people.name, people.lastname)
            }
        }
    }
}


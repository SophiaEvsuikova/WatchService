package com.watchservice

import org.springframework.stereotype.Service
import java.io.*
import java.nio.file.Paths
import java.sql.Connection
import java.sql.DriverManager
import java.sql.ResultSet
import java.util.*

@Service
class DataBaseService{

    private val connection: Connection

    init {
        //FIXME вместо этого можно использовать инъекцию через @Value
        val props = Properties()
        val file = File(Paths.get("").toAbsolutePath().toString() +
                "\\src\\main\\resources\\application.properties")
        FileInputStream(file).use { props.load(it) }
        connection = DriverManager.getConnection(props.getProperty("url"), props)
    }

    fun insert(name: String, lastname: String)
    {
        val insertStatement = connection.prepareStatement(
            "INSERT INTO public.personthirdtask(name, lastname) VALUES (?, ?)")

        insertStatement.setString(1, name)
        insertStatement.setString(2, lastname)
        insertStatement.executeUpdate()
    }

    fun findPerson(name: String, lastname: String): Int {
        val selectStatement = connection.prepareStatement(
            "SELECT * FROM public.personthirdtask WHERE (name = ? and lastname = ?)",
            ResultSet.TYPE_SCROLL_INSENSITIVE,
            ResultSet.CONCUR_UPDATABLE)
        selectStatement.setString(1, name)
        selectStatement.setString(2, lastname)
        val resultSet = selectStatement.executeQuery()
        var size = 0
        if (resultSet != null) {
            resultSet.last()
            size = resultSet.getRow()
        }
        return size
    }
}
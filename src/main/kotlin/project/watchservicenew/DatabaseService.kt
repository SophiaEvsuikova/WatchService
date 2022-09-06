package project.watchservicenew

import kotlinx.serialization.Serializable
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.stereotype.Service
import java.sql.Connection
import java.sql.DriverManager
import java.sql.ResultSet


@Serializable
data class PersonData(val id: Int, val name: String, val lastname: String)

@Service
class DataBaseService()
{

    val jdbcUrl = "jdbc:postgresql://localhost:5432/postgres"

    fun insert(name: String, lastname: String)
    {
        val connection = DriverManager
            .getConnection(jdbcUrl, "postgres", "12345SE")
        val insertStatement = connection.prepareStatement(
            "INSERT INTO public.personthirdtask(name, lastname) VALUES (?, ?)")

        insertStatement.setString(1, name)
        insertStatement.setString(2, lastname)
        insertStatement.executeUpdate()
    }

    fun findPerson(name: String, lastname: String): Int {


        val connection = DriverManager
            .getConnection(jdbcUrl, "postgres", "12345SE")

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
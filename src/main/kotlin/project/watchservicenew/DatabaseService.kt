package project.watchservicenew

import kotlinx.serialization.Serializable
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.stereotype.Component
import java.sql.Connection
import java.sql.DriverManager
import java.sql.ResultSet

@Serializable
data class PersonData(val name: String, val lastname: String)

@Component
class DataBaseService (@Value("\${app.url}") val url: String,
                       @Value("\${app.username}") val username: String,
                       @Value("\${app.password}") val password: String) {

    val connection: Connection = DriverManager
        .getConnection(url, username, password)

    /*
    TODO когда работаем со спрингом лучше сразу использовать JdbcTemplate, а не стандартные классы из java.sql
    @Autowired
    private val jdbcTemplate: JdbcTemplate
    */

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
package project.watchservicenew

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.CommandLineRunner
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.stereotype.Component
import java.io.Console

@SpringBootApplication
class WatchServiceNewApplication

fun main(args: Array<String>) {
    runApplication<WatchServiceNewApplication>(*args)
}

@Component
class Main : CommandLineRunner {

    @Value("\${url}")
    val url: String = ""

    @Autowired
    var fileWatch : FileWatch = FileWatch()

    private val coroutineScope = CoroutineScope(Dispatchers.Default)
    override fun run(vararg args: String) {

        coroutineScope.launch {
            fileWatch.launchWatchingAsync()
        }

        readLine()

    }
}
package project.watchservicenew

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.Serializable
import mu.KotlinLogging
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.stereotype.Service
import java.nio.file.*
import java.nio.file.StandardWatchEventKinds.ENTRY_CREATE



@Service
class FileWatch {



    @Value("\${url}")
    val url: String = ""

    private val logger = KotlinLogging.logger {}
    //private val injectedDispatcher: CoroutineDispatcher = Dispatchers.IO


    suspend fun launchWatchingAsync() {


        val fileReader = FileReader()
        val watcher: WatchService = withContext(Dispatchers.IO) {
            FileSystems.getDefault().newWatchService()
        }
        val path: Path = Paths.get("D:\\Repositories\\watchServiceNew\\src\\main\\kotlin\\project\\assets")
        withContext(Dispatchers.IO) {
            path.register(watcher, ENTRY_CREATE)
        }

        while (true) {
            try {
                val key: WatchKey = watcher.take()
                val watchEvents = key.pollEvents()

                for (event in watchEvents) {
                    val kind = event.kind()
                    if (ENTRY_CREATE.equals(kind)) {
                       val obj = fileReader.read(event.context().toString())
                       fileReader.getPerson(obj)
                    }
                }
                key.reset()
            } catch (ex: Exception) {
                logger.error(ex.message) { "Exception: File not found" }
            }
        }

    }
}




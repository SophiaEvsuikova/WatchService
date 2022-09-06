package project.watchservicenew

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import mu.KotlinLogging
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import java.nio.file.*
import java.nio.file.StandardWatchEventKinds.ENTRY_CREATE


@Service
class FileWatch(@Value("\${app.filepath}") val filepath: String) {

    private val logger = KotlinLogging.logger {}

    @Autowired
    lateinit var fileReader : FileReader
    suspend fun launchWatchingAsync() {

        val watcher: WatchService = withContext(Dispatchers.IO) {
            FileSystems.getDefault().newWatchService()
        }
        val path: Path = Paths.get(filepath)
        withContext(Dispatchers.IO) {
            path.register(watcher, ENTRY_CREATE)
        }

        while (true) {
            try {
                val key: WatchKey = withContext(Dispatchers.IO) {
                    watcher.take()
                }
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




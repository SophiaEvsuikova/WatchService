package com.watchservice

import java.nio.file.*
import java.nio.file.StandardWatchEventKinds.*

class FileWatch {
    fun launchWatching () {

        val fileReader = FileReader()
        val watcher: WatchService = FileSystems.getDefault().newWatchService()
        val path: Path = Paths.get("D:\\Repositories\\WatchService\\src\\main\\kotlin\\assets")
        path.register(watcher, ENTRY_CREATE)

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
                    key.reset()
                }
            }
            catch (ex : Exception)
            {
                println(ex.message)
            }
        }
    }
}

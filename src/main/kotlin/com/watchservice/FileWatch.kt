package com.watchservice

import java.nio.file.*
import java.nio.file.StandardWatchEventKinds.*

class FileWatch {
    fun launchWatching () {

        val fileReader = FileReader()
        val watcher: WatchService = FileSystems.getDefault().newWatchService()
        // TODO пути следует выносить в .properties файл
        val path: Path = Paths.get("D:\\Repositories\\WatchService\\src\\main\\kotlin\\assets")
        path.register(watcher, ENTRY_CREATE)

        //FIXME для самостоятельного приложения, выполняющего одну функцию "прослушивание директории" - это сойдет
        // - в большом приложении, выполняющим множество функций, такой подход нереален, тк это будет блокировать основной поток
        // - рекомендую ознакомиться с многопоточностью в Java/Kotlin и подумать, как это здесь можно применить, чтобы не блокировать основной поток
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
                // TODO в целях логгирования лучше использовать соответствующие библиотеки
                println(ex.message)
            }
        }
    }
}

package com.watchservice

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class WatchServiceApplication

fun main(args: Array<String>) {
    runApplication<WatchServiceApplication>(*args)
}

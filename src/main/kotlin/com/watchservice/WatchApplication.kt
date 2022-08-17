package com.watchservice

//FIXME все таки рекомендуется использовать Spring Boot
// - заодно это упростит использование подключения к БД
fun main() {
    val fileWatch = FileWatch()
    println(fileWatch.launchWatching())
}

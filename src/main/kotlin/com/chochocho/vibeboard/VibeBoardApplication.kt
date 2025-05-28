package com.chochocho.vibeboard

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class VibeBoardApplication

fun main(args: Array<String>) {
    runApplication<VibeBoardApplication>(*args)
}

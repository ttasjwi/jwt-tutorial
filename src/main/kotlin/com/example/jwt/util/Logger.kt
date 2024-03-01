package com.example.jwt.util

import io.github.oshai.kotlinlogging.KotlinLogging

fun getLogger(clazz: Class<Any>) = KotlinLogging.logger(clazz.name)

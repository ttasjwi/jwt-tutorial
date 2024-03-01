package com.example.jwt.error

import com.example.jwt.error.dto.ErrorApi
import com.example.jwt.util.getLogger
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class ExceptionAdvice {

    private val logger = getLogger(javaClass)

    @ExceptionHandler(Exception::class)
    fun handleException(ex: Exception): ResponseEntity<ErrorApi> {
        logger.error(ex){ "예상치 못 한 예외"}

        return ResponseEntity
            .status(HttpStatus.INTERNAL_SERVER_ERROR)
            .body(ErrorApi("예상치 못한 예외가 발생했습니다."))
    }

}

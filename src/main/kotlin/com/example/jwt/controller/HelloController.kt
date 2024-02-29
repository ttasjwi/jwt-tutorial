package com.example.jwt.controller

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class HelloController {

    @GetMapping("/api/hello")
    fun hello(): ResponseEntity<String> {
        return ResponseEntity.ok("hello")
    }


}
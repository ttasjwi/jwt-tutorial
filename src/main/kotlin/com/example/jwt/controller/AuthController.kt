package com.example.jwt.controller

import com.example.jwt.dto.LoginDto
import com.example.jwt.dto.TokenDto
import com.example.jwt.jwt.TokenAuthenticationFilter
import com.example.jwt.jwt.TokenProvider
import com.example.jwt.util.getLogger
import jakarta.validation.Valid
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController
class AuthController(
    private val authenticationManagerBuilder: AuthenticationManagerBuilder,
    private val tokenProvider: TokenProvider
) {
    private val logger = getLogger(javaClass)

    @PostMapping("/api/authenticate")
    fun authorize(@Valid @RequestBody loginDto: LoginDto) : ResponseEntity<TokenDto> {

        logger.info { "로그인 시도" }

        val authentication = UsernamePasswordAuthenticationToken(loginDto.username, loginDto.password)
        val authenticationResult = authenticationManagerBuilder.`object`.authenticate(authentication)

        SecurityContextHolder.getContext().authentication = authenticationResult

        val jwt = tokenProvider.createToken(authenticationResult)

        val headers = HttpHeaders()
        headers.add(TokenAuthenticationFilter.AUTHORIZATION_HEADER, "Bearer ${jwt}")

        return ResponseEntity(TokenDto(jwt), headers, HttpStatus.OK)
    }
}

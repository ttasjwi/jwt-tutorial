package com.example.jwt.jwt

import com.example.jwt.util.getLogger
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.http.HttpStatus
import org.springframework.security.core.AuthenticationException
import org.springframework.security.web.AuthenticationEntryPoint

class JwtAuthenticationEntryPoint : AuthenticationEntryPoint{

    private val logger = getLogger(javaClass)

    override fun commence(
        request: HttpServletRequest?,
        response: HttpServletResponse?,
        authException: AuthenticationException?,
    ) {

        logger.info { "인증 실패" }
        response!!.sendError(HttpStatus.UNAUTHORIZED.value())
    }
}

package com.example.jwt.jwt

import com.example.jwt.util.getLogger
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.http.HttpStatus
import org.springframework.security.access.AccessDeniedException
import org.springframework.security.web.access.AccessDeniedHandler


class JwtAccessDeniedHandler : AccessDeniedHandler {

    private val logger = getLogger(javaClass)

    override fun handle(
        request: HttpServletRequest?,
        response: HttpServletResponse?,
        accessDeniedException: AccessDeniedException?,
    ) {
        logger.debug(accessDeniedException) { "인가 실패...!" }
        response!!.sendError(HttpStatus.FORBIDDEN.value())
    }

}

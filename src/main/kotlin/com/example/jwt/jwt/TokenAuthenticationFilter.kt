package com.example.jwt.jwt

import com.example.jwt.util.getLogger
import jakarta.servlet.FilterChain
import jakarta.servlet.ServletRequest
import jakarta.servlet.ServletResponse
import jakarta.servlet.http.HttpServletRequest
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.util.StringUtils
import org.springframework.web.filter.GenericFilterBean


class TokenAuthenticationFilter(

    private val tokenProvider: TokenProvider,

    ) : GenericFilterBean() {

    private val log = getLogger(javaClass)

    companion object {
        const val AUTHORIZATION_HEADER = "Authorization"
    }

    override fun doFilter(rq: ServletRequest?, rsp: ServletResponse?, chain: FilterChain?) {
        val request = rq as HttpServletRequest
        val jwt = resolveToken(request)!!
        val requestURI = request.requestURI!!

        if (StringUtils.hasText(jwt) && tokenProvider.validateToken(jwt!!)) {
            val authentication = tokenProvider.getAuthentication(jwt)

            SecurityContextHolder.getContext().authentication = authentication
            log.debug { "Security Context에 '${authentication.name}' 인증정보를 저장했습니다. uri: ${requestURI}" }
        } else {
            log.debug { "유효한 JWT 토큰이 없습니다. uri: ${requestURI}" }
        }
        chain?.doFilter(rq, rsp)
    }

    private fun resolveToken(request: HttpServletRequest): String? {
        val bearerToken = request.getHeader(AUTHORIZATION_HEADER)
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7)
        }
        return null
    }
}

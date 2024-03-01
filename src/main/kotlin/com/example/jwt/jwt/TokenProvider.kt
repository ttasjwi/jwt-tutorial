package com.example.jwt.jwt

import com.example.jwt.util.getLogger
import io.jsonwebtoken.ExpiredJwtException
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.MalformedJwtException
import io.jsonwebtoken.UnsupportedJwtException
import io.jsonwebtoken.io.Decoders
import io.jsonwebtoken.security.Keys
import org.springframework.beans.factory.annotation.Value
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.User
import org.springframework.stereotype.Component
import java.security.Key
import java.util.*
import javax.crypto.SecretKey


/**
 * 토큰의 생성, 유효성 검증을 담당
 */
@Component
class TokenProvider(

    @Value("\${jwt.token-validity-in-seconds}")
    tokenValidityInSeconds: Long,

    @Value("\${jwt.secret}")
    private val secret: String,
) {

    private val logger = getLogger(javaClass)
    private val tokenValidityInMilliSeconds: Long = tokenValidityInSeconds * 1000
    private val key: Key = Keys.hmacShaKeyFor(Decoders.BASE64.decode(secret))

    companion object {

        private const val ROLES_KEY = "roles"
    }

    fun createToken(authentication: Authentication): String {
        return Jwts.builder()
            .subject(authentication.name)
            .claim(ROLES_KEY, getRolesString(authentication)) // 권한(또는 역할)을 문자열로 풀고 claim에 담기
            .signWith(key)
            .expiration(generateExpireTime())
            .compact()
    }

    private fun getRolesString(authentication: Authentication) = authentication
        .authorities
        .joinToString(separator = ",") { it.authority }

    private fun generateExpireTime(): Date {
        val now = Date().time
        return Date(now + tokenValidityInMilliSeconds)
    }

    fun getAuthentication(token: String): Authentication {
        val claims = Jwts
            .parser()
            .verifyWith(key as SecretKey)
            .build()
            .parseSignedClaims(token)
            .payload

        val authorities = claims[ROLES_KEY].toString().split(",")
            .map { SimpleGrantedAuthority(it) }
            .toList()

        val principal = User(claims.subject, "", authorities)
        return UsernamePasswordAuthenticationToken.authenticated(principal, token, authorities)
    }


    fun validateToken(token: String): Boolean {
        try {
            // 토큰을 파라미터로 받아서 파싱시도
            Jwts.parser()
                .verifyWith(key as SecretKey)
                .build()
                .parseSignedClaims(token)
            return true
        } catch (e: io.jsonwebtoken.security.SecurityException) {
            // 암호화 키, 알고리즘 또는 기본 Java JCA API와 같은 보안 관련 요소 문제로 인해 발생하는 예외
            logger.error { "잘못된 JWT 서명입니다." }
        } catch (e: MalformedJwtException) {

            // JWT가 올바르게 구성되지 않았으므로 거부해야 함을 나타내는 예외
            logger.info { "잘못된 JWT 서명입니다." }

        } catch (e: UnsupportedJwtException) {
            // 애플리케이션에서 예상하는 형식과 일치하지 않는 특정 형식/구성으로 JWT를 수신할 때 예외가 발생
            // 암호화된 jwt를 기대했는데, 암호화 되지 않은 jwt가 왔다거나...
            logger.error(e) { "유효하지 않은 JWT 서명입니다." }
        } catch (e: ExpiredJwtException) {
            logger.error(e) { "만료된 JWT 토큰" }
        } catch (e: IllegalArgumentException) {
            // 시그니처 또는 페이로드가 비어있는 경우
            logger.error(e) { "유효하지 않은 JWT 서명입니다." }
        } catch (e: Exception) {
            logger.error(e) { "예상치 못 한 예외!" }
        }
        return false
    }

}

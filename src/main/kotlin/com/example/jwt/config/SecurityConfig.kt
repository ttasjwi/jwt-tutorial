package com.example.jwt.config

import com.example.jwt.jwt.JwtAccessDeniedHandler
import com.example.jwt.jwt.JwtAuthenticationEntryPoint
import com.example.jwt.jwt.TokenAuthenticationFilter
import com.example.jwt.jwt.TokenProvider
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer
import org.springframework.security.config.annotation.web.invoke
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter

@Configuration
class SecurityConfig(
    private val tokenProvider: TokenProvider,
) {

    @Bean
    fun securityFilterChain(http: HttpSecurity): SecurityFilterChain {
        http {

            csrf { disable() }

            authorizeHttpRequests {
                authorize("/api/hello", permitAll)
                authorize("/api/authenticate", permitAll)
                authorize("/api/signup", permitAll)
                authorize(anyRequest, authenticated)
            }

            headers {
                frameOptions { sameOrigin }
            }

            sessionManagement {
                sessionCreationPolicy = SessionCreationPolicy.STATELESS
            }

            exceptionHandling {
                authenticationEntryPoint = jwtAuthenticationEntryPoint()
                accessDeniedHandler = jwtAccessDeniedHandler()
            }

            addFilterBefore<UsernamePasswordAuthenticationFilter>(tokenAuthenticationFilter())
        }
        return http.build()
    }

    @Bean
    fun webSecurityCustomizer() = WebSecurityCustomizer { web ->
        web.ignoring().requestMatchers("/h2-console/**", "/favicon.ico")
    }

    @Bean
    fun tokenAuthenticationFilter() = TokenAuthenticationFilter(tokenProvider)


    @Bean
    fun jwtAuthenticationEntryPoint() = JwtAuthenticationEntryPoint()

    @Bean
    fun jwtAccessDeniedHandler() = JwtAccessDeniedHandler()

    @Bean
    fun passwordEncoder() = BCryptPasswordEncoder()
}

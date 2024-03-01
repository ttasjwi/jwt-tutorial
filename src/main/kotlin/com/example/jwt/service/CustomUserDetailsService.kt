package com.example.jwt.service

import com.example.jwt.entity.User
import com.example.jwt.repository.UserRepository
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Component

@Component(value = "userDetailsService")
class CustomUserDetailsService(
    private val userRepository: UserRepository
): UserDetailsService {

    override fun loadUserByUsername(username: String?): UserDetails {
        val user = userRepository.findOneWithAuthoritiesByUsername(username!!)
                ?: throw UsernameNotFoundException("${username} -> 데이터베이스에서 찿을 수 없습니다.")

        return createSecurityUser(username, user)
    }

    private fun createSecurityUser(username: String, user: User) : org.springframework.security.core.userdetails.User {
        if (!user.activated) {
            throw RuntimeException("${username} -> 활성화되어 있지 않습니다.")
        }

        val grantedAuthorities =  user.authorities
            .map { authority -> SimpleGrantedAuthority(authority.name) }
            .toSet()

        return org.springframework.security.core.userdetails.User(user.username, user.password, grantedAuthorities)
    }
}

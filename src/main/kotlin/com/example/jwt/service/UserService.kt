package com.example.jwt.service

import com.example.jwt.dto.UserDto
import com.example.jwt.entity.Authority
import com.example.jwt.entity.User
import com.example.jwt.repository.UserRepository
import com.example.jwt.util.SecurityUtil
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class UserService (

    private val userRepository: UserRepository,
    private val passwordEncoder: PasswordEncoder
) {

    @Transactional
    fun signUp(userDto: UserDto): User {
        if (userRepository.existsByUsername(userDto.username!!)) {
            throw RuntimeException("이미 가입되어 있는 유저입니다.")
        }

        val authority = Authority("ROLE_USER")

        val user = User(
            username = userDto.username,
            password = passwordEncoder.encode(userDto.password),
            nickname = userDto.nickname!!,
            authorities = mutableSetOf(authority),
            activated = true
        )
        return userRepository.save(user)
    }

    @Transactional(readOnly = true)
    fun getUserWithAuthorities(username: String): User {
        return userRepository.findOneWithAuthoritiesByUsername(username) ?: throw RuntimeException("일치하는 이름의 사용자를 찾을 수 없습니다")
    }

    @Transactional(readOnly = true)
    fun getMeWithAuthorities() : User? {
        val currentUsername = SecurityUtil.getCurrentUsername()
        if (currentUsername === null) {
            return null
        }
        return userRepository.findOneWithAuthoritiesByUsername(currentUsername)
    }

}

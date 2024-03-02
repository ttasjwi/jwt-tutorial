package com.example.jwt.controller

import com.example.jwt.controller.dto.MyUserInfoResponse
import com.example.jwt.controller.dto.SignUpResponse
import com.example.jwt.controller.dto.UserInfoResponse
import com.example.jwt.dto.UserDto
import com.example.jwt.service.UserService
import com.example.jwt.util.getLogger
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController
class UserController (
    private val userService: UserService
) {

    private val logger = getLogger(javaClass)

    @PostMapping("/api/users")
    fun signUp(@Valid @RequestBody userDto: UserDto) : ResponseEntity<SignUpResponse> {
        logger.debug { "userDto = ${userDto}" }

        val user = userService.signUp(userDto)
        return ResponseEntity
            .status(HttpStatus.CREATED)
            .body(SignUpResponse.from(user))
    }

    @GetMapping("/api/me")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    fun getMyUserInfo() : ResponseEntity<MyUserInfoResponse> {
        val user = userService.getMeWithAuthorities()!!

        return ResponseEntity
            .status(HttpStatus.OK)
            .body(MyUserInfoResponse.from(user))
    }

    @GetMapping("/api/admin/users/{username}")
    @PreAuthorize("hasAnyRole('ADMIN')")
    fun getUserInfo(@PathVariable username: String) : ResponseEntity<UserInfoResponse> {
        val user = userService.getUserWithAuthorities(username)
        return ResponseEntity
            .status(HttpStatus.OK)
            .body(UserInfoResponse.from(user))
    }
}

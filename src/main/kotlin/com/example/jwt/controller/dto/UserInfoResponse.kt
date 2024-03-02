package com.example.jwt.controller.dto

import com.example.jwt.entity.User

class UserInfoResponse(

    val userId: Long,
    val username: String,
    val nickname: String,
    val authorities: List<String>,
) {

    companion object {

        fun from(user: User): UserInfoResponse {
            return UserInfoResponse(
                userId= user.id!!,
                username= user.username,
                nickname = user.nickname,
                authorities = user.authorities.map { it.name }
            )
        }
    }
}

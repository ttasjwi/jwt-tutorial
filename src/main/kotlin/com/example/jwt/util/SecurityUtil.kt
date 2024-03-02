package com.example.jwt.util

import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.userdetails.UserDetails

class SecurityUtil {

    private val logger = getLogger(javaClass)

    companion object {
        fun getCurrentUsername(): String? {
            val authentication = SecurityContextHolder.getContext().authentication

            if (authentication === null) {
                return null
            }

            var username: String? = null
            if (authentication.principal is UserDetails) {
                val userDetails = authentication.principal as UserDetails
                username = userDetails.username as String
            } else if (authentication.principal is String) {
                username = authentication.principal as String
            }
            return username
        }
    }
}

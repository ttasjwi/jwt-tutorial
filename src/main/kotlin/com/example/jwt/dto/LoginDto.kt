package com.example.jwt.dto

import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Size

data class LoginDto (

    @NotNull
    @Size(min = 3, max = 50)
    val username: String?,

    @NotNull
    @Size(min = 3, max = 100)
    val password: String?,

)

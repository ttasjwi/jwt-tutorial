package com.example.jwt.dto

import com.fasterxml.jackson.annotation.JsonProperty
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Size

data class UserDto (

    @NotNull
    @Size(min = 3, max= 50)
    val username: String?,

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @NotNull
    @Size(min = 3, max= 100)
    val password: String?,

    @NotNull
    @Size(min = 3, max = 50)
    val nickname: String?,
)

package cz.cvut.fit.sp1.api.security.data.dto

import jakarta.validation.constraints.NotEmpty

data class JwtRequest(
    @field:NotEmpty(message = "Login cannot be empty")
    var login: String? = null,
    @field:NotEmpty(message = "Password cannot be empty")
    val password: String? = null,
)

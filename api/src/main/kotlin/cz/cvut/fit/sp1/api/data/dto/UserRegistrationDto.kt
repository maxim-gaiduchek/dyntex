package cz.cvut.fit.sp1.api.data.dto

import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotEmpty

data class UserRegistrationDto(
    @NotEmpty(message = "Name must not be empty")
    var name: String,
    @NotEmpty(message = "Email must not be empty")
    @Email(message = "Invalid email format")
    var email: String,
    @NotEmpty(message = "Password must not be empty")
    var password: String,
)

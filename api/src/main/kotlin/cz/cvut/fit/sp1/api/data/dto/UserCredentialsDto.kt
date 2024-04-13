package cz.cvut.fit.sp1.api.data.dto

import cz.cvut.fit.sp1.api.validation.group.UserLoginGroup
import cz.cvut.fit.sp1.api.validation.group.UserRegistrationGroup
import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotEmpty

data class UserCredentialsDto(
    @NotEmpty(message = "Name must not be empty", groups = [UserRegistrationGroup::class])
    var name: String,
    @NotEmpty(message = "Email must not be empty", groups = [UserRegistrationGroup::class, UserLoginGroup::class])
    @Email(message = "Invalid email format", groups = [UserRegistrationGroup::class, UserLoginGroup::class])
    var email: String,
    @NotEmpty(message = "Password must not be empty", groups = [UserRegistrationGroup::class, UserLoginGroup::class])
    var password: String,
)

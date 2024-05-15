package cz.cvut.fit.sp1.api.data.dto

import cz.cvut.fit.sp1.api.validation.group.UserLoginGroup
import cz.cvut.fit.sp1.api.validation.group.UserRecoveryGroup
import cz.cvut.fit.sp1.api.validation.group.UserRecoveryUpdateGroup
import cz.cvut.fit.sp1.api.validation.group.UserRegistrationGroup
import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotEmpty

data class UserCredentialsDto(
    @field:NotEmpty(message = "Name must not be empty", groups = [UserRegistrationGroup::class])
    var name: String?,
    @field:NotEmpty(
        message = "Email must not be empty",
        groups = [UserRegistrationGroup::class, UserLoginGroup::class, UserRecoveryGroup::class]
    )
    @field:Email(
        message = "Invalid email format",
        groups = [UserRegistrationGroup::class, UserLoginGroup::class, UserRecoveryGroup::class]
    )
    var email: String?,
    @field:NotEmpty(
        message = "Password must not be empty",
        groups = [UserRegistrationGroup::class, UserLoginGroup::class, UserRecoveryUpdateGroup::class]
    )
    var password: String?,
    @field:NotEmpty(
        message = "Authentication token must not be empty",
        groups = [UserRecoveryUpdateGroup::class]
    )
    var authToken: String?
)

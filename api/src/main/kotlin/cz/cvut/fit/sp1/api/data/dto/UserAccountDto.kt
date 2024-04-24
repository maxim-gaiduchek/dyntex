package cz.cvut.fit.sp1.api.data.dto

import cz.cvut.fit.sp1.api.data.model.AccountRole
import cz.cvut.fit.sp1.api.validation.group.UpdateGroup
import jakarta.validation.constraints.NotEmpty

data class UserAccountDto(
    val id: Long?,
    @field:NotEmpty(message = "User name must not be empty", groups = [UpdateGroup::class])
    var name: String?,
    var email: String?,
    var role: AccountRole?,
    var likedMedia: MutableList<MediaDto>? = mutableListOf(),
    var createdMedia: MutableList<MediaDto>? = mutableListOf(),
    var avatar: AvatarDto?,
    var token: String?
)

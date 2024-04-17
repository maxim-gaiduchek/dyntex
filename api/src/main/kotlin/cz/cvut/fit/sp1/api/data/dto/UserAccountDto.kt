package cz.cvut.fit.sp1.api.data.dto

import cz.cvut.fit.sp1.api.data.model.AccountRole
import cz.cvut.fit.sp1.api.validation.group.UpdateGroup
import jakarta.validation.constraints.NotEmpty

data class UserAccountDto(
    val id: Long?,
    @NotEmpty(groups = [UpdateGroup::class], message = "User name must not be empty")
    var name: String,
    var email: String,
    var role: AccountRole,
    var likedMedia: MutableList<MediaDto> = mutableListOf(),
    var createdMedia: MutableList<MediaDto> = mutableListOf()
)

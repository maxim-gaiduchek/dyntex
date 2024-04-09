package cz.cvut.fit.sp1.api.data.dto

import cz.cvut.fit.sp1.api.data.model.AccountRole
import cz.cvut.fit.sp1.api.data.model.Tag

data class UserAccountDto(
    var name: String = "",
    var email: String = "",
    var role: AccountRole = AccountRole.USER,
    var likedMedia: MutableList<MediaDto> = mutableListOf(),
    var createdMedia: MutableList<MediaDto> = mutableListOf(),
    var createdTags: MutableList<Tag> = mutableListOf(),
)

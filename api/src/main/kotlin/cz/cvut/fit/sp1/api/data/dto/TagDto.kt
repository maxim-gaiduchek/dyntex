package cz.cvut.fit.sp1.api.data.dto

import cz.cvut.fit.sp1.api.validation.group.CreateGroup
import jakarta.validation.constraints.NotEmpty

data class TagDto(
    var id: Long?,
    @field:NotEmpty(message = "Tag name must not be empty", groups = [CreateGroup::class])
    var name: String?,
    @field:NotEmpty(message = "Tag emoji must not be empty", groups = [CreateGroup::class])
    var emoji: String?,
)

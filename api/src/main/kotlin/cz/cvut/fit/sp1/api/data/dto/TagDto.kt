package cz.cvut.fit.sp1.api.data.dto

import cz.cvut.fit.sp1.api.validation.group.CreateGroup
import cz.cvut.fit.sp1.api.validation.group.UsedInOther
import jakarta.validation.constraints.NotEmpty
import jakarta.validation.constraints.NotNull

data class TagDto(
    @field:NotNull(message = "Tag id must not be null", groups = [UsedInOther::class])
    var id: Long?,
    @field:NotEmpty(message = "Tag name must not be empty", groups = [CreateGroup::class])
    var name: String,
    @field:NotEmpty(message = "Tag emoji must not be empty", groups = [CreateGroup::class])
    var emoji: String,
)

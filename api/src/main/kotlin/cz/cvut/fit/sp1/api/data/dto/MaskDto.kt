package cz.cvut.fit.sp1.api.data.dto

import cz.cvut.fit.sp1.api.validation.group.CreateGroup
import cz.cvut.fit.sp1.api.validation.group.UpdateGroup
import jakarta.validation.constraints.NotEmpty

data class MaskDto(
    @field:NotEmpty(message = "Mask ID must not be empty", groups = [UpdateGroup::class])
    override var id: Long?,
    @field:NotEmpty(message = "Mask name must not be empty", groups = [CreateGroup::class, UpdateGroup::class])
    override var name: String?,
    override var path: String?,
    override var size: String?,
    override var description: String = "",
    override var format: String?,
    override var createdBy: UserAccountDto?,
    override var createdById: Long?,
    override var likes: Int?,
    @field:NotEmpty(message = "Video must have at least one tag", groups = [CreateGroup::class, UpdateGroup::class])
    override var tags: MutableList<TagDto> = mutableListOf(),
    var tagIds: MutableList<Long>?,
) : MediaDto(id, name, size, description, path, format, createdBy, createdById, likes, tags)

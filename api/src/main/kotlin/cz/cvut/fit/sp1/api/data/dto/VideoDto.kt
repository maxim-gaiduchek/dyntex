package cz.cvut.fit.sp1.api.data.dto

import cz.cvut.fit.sp1.api.validation.group.CreateGroup
import cz.cvut.fit.sp1.api.validation.group.UpdateGroup
import jakarta.validation.constraints.NotEmpty

data class VideoDto(
    var fps: Long?,
    var duration: Double?,
    @field:NotEmpty(message = "Video ID must not be empty", groups = [UpdateGroup::class])
    override var id: Long?,
    @field:NotEmpty(message = "Video name must not be empty", groups = [CreateGroup::class, UpdateGroup::class])
    override var name: String?,
    override var path: String?,
    var previewPath: String?,
    override var size: String?,
    override var description: String = "",
    override var format: String?,
    override var createdBy: UserAccountDto?,
    override var createdById: Long?,
    override var likes: Int?,
    override var tags: MutableList<TagDto> = mutableListOf(),
    var likedByUserIds: MutableList<Long> = mutableListOf(),
    @field:NotEmpty(message = "Video must have at least one tag", groups = [CreateGroup::class, UpdateGroup::class])
    override var tagIds: MutableList<Long>?,
) : MediaDto(id, name, size, description, path, format, createdBy, createdById, likes, tags, tagIds)

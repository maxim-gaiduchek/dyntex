package cz.cvut.fit.sp1.api.data.dto

import cz.cvut.fit.sp1.api.validation.group.CreateGroup
import jakarta.validation.constraints.NotEmpty

data class VideoDto(
    var fps: Long?,
    var duration: Double?,
    override var id: Long?,
    override var name: String?,
    override var path: String?,
    var previewPath: String?,
    override var size: String?,
    override var description: String = "",
    override var format: String?,
    override var createdById: Long?,
    override var likes: Int?,
    override var tags: MutableList<TagDto> = mutableListOf(),
    @field:NotEmpty(message = "Video must have at least one tag", groups = [CreateGroup::class])
    var tagIds: MutableList<Long>?,
) : MediaDto(id, name, size, description, path, format, createdById, likes, tags)

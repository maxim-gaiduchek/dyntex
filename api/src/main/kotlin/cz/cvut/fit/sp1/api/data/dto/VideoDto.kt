package cz.cvut.fit.sp1.api.data.dto

import cz.cvut.fit.sp1.api.validation.group.CreateGroup
import cz.cvut.fit.sp1.api.validation.group.UsedInOther
import jakarta.validation.Valid
import jakarta.validation.constraints.NotEmpty
import jakarta.validation.groups.ConvertGroup

data class VideoDto(
    var fps: Long?,
    var duration: Double,
    override var id: Long?,
    override var name: String,
    override var path: String,
    var previewPath: String?,
    override var size: String,
    override var description: String?,
    override var format: String,
    override var createdById: Long,
    override var likes: Int,
    @field:NotEmpty(message = "Video must have at least one tag", groups = [CreateGroup::class])
    @field:ConvertGroup(from = CreateGroup::class, to = UsedInOther::class)
    @field:Valid
    override var tags: MutableList<TagDto> = mutableListOf(),
) : MediaDto(id, name, size, description, path, format, createdById, likes, tags)

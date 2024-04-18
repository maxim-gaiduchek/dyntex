package cz.cvut.fit.sp1.api.data.dto

data class VideoDto(
    var fps: Long?,
    var duration: Double,
    override var id: Long?,
    override var name: String,
    override var path: String,
    override var size: String,
    override var description: String,
    override var format: String,
    override var createdById: Long,
    override var likes: Int,
    override var tags: MutableList<TagDto> = mutableListOf(),
) : MediaDto(id, name, size, description, path, format, createdById, likes, tags)

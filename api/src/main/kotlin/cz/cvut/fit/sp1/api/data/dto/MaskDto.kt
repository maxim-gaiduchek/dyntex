package cz.cvut.fit.sp1.api.data.dto

data class MaskDto(
    override var id: Long?,
    override var name: String?,
    override var path: String?,
    override var size: String?,
    override var description: String = "",
    override var format: String?,
    override var createdBy: UserAccountDto?,
    override var createdById: Long?,
    override var likes: Int?,
    override var tags: MutableList<TagDto> = mutableListOf(),
) : MediaDto(id, name, size, description, path, format, createdBy, createdById, likes, tags)

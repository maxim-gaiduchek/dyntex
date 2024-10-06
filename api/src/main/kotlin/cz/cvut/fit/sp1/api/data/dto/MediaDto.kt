package cz.cvut.fit.sp1.api.data.dto

open class MediaDto(
    open var id: Long?,
    open var name: String?,
    open var size: String?,
    open var description: String,
    open var path: String?,
    open var format: String?,
    open var createdBy: UserAccountDto?,
    open var createdById: Long?,
    open var likes: Int?,
    open var tags: MutableList<TagDto> = mutableListOf(),
    open var tagIds: MutableList<Long>?,
)

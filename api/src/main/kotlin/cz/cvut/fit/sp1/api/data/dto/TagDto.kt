package cz.cvut.fit.sp1.api.data.dto

data class TagDto(
    var id: Long?,
    var name: String,
    var emoji: String,
    var createdBy: UserAccountDto? = null,
    var media: MutableList<MediaDto> = mutableListOf(),
)

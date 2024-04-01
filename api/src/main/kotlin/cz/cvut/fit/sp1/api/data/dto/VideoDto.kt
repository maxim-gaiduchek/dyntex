package cz.cvut.fit.sp1.api.data.dto

data class VideoDto(
    val id: Long?,
    val name: String,
    val fps: Long?,
    val width: Int?,
    val height: Int?,
    val path: String,
    val extension: String,
    val duration: Long?,
)

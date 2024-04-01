package cz.cvut.fit.sp1.api.data.dto

import cz.cvut.fit.sp1.api.data.model.media.Video

data class VideoDto(
    val name: String,
    val fps: Long?,
    val width: Int?,
    val height: Int?,
    val path: String,
    val extension: String,
    val duration: Long?,
)

fun VideoDto.toEntity(): Video {
    val video =
        Video(
            name = this.name,
            path = this.path,
            format = this.extension, // Предполагается, что extension это формат файла
        )
    video.duration = this.duration ?: 0L
    video.fps = this.fps ?: 0L // Пример преобразования Rational в Long, предполагается метод toLong()
    return video
}

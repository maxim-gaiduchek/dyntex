package cz.cvut.fit.sp1.api.component.mapper

import cz.cvut.fit.sp1.api.data.dto.VideoDto
import cz.cvut.fit.sp1.api.data.model.media.Video
import org.mapstruct.AfterMapping
import org.mapstruct.Mapper
import org.mapstruct.Mapping
import org.mapstruct.MappingTarget
import org.mapstruct.ReportingPolicy
import org.mapstruct.*

@Mapper(
    componentModel = "spring",
    unmappedTargetPolicy = ReportingPolicy.IGNORE,
    uses = [UserAccountMapper::class, TagMapper::class]
)
abstract class VideoMapper {

    @Mapping(target = "createdById", source = "createdBy.id")
    abstract fun toDto(video: Video?): VideoDto?

    abstract fun toEntity(videoDto: VideoDto?): Video?

    @AfterMapping
    fun enrichWithLikes(@MappingTarget videoDto: VideoDto, video: Video?) {
        videoDto.likes = video?.likedBy?.size ?: 0
    }
    // Define mapping for tagIds from Video to VideoDto if necessary.
    // This is a placeholder and needs actual implementation based on how Video entity relates to tags.
    @AfterMapping
    fun enrichWithTagIds(@MappingTarget videoDto: VideoDto, video: Video?) {
        videoDto.tagIds = video?.tags?.map { it.id }?.toMutableList() ?: mutableListOf()
    }
}
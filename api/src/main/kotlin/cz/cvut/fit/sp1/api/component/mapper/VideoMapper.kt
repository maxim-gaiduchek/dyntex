package cz.cvut.fit.sp1.api.component.mapper

import cz.cvut.fit.sp1.api.data.dto.VideoDto
import cz.cvut.fit.sp1.api.data.model.media.Video
import org.mapstruct.AfterMapping
import org.mapstruct.Mapper
import org.mapstruct.Mapping
import org.mapstruct.MappingTarget
import org.mapstruct.ReportingPolicy

@Mapper(
    componentModel = "spring",
    unmappedTargetPolicy = ReportingPolicy.IGNORE,
    uses = [TagMapper::class]
)
abstract class VideoMapper {

    @Mapping(target = "createdBy.likedVideos", ignore = true)
    @Mapping(target = "createdBy.likedMasks", ignore = true)
    @Mapping(target = "createdBy.createdVideos", ignore = true)
    @Mapping(target = "createdBy.createdMasks", ignore = true)
    @Mapping(target = "createdBy.token", ignore = true)
    abstract fun toDto(video: Video?): VideoDto?

    abstract fun toEntity(videoDto: VideoDto?): Video?

    @AfterMapping
    fun enrichWithLikes(@MappingTarget videoDto: VideoDto, video: Video?) {
        videoDto.likes = video?.likedBy?.size ?: 0
    }
}
package cz.cvut.fit.sp1.api.component.mapper

import cz.cvut.fit.sp1.api.data.dto.MediaDto
import cz.cvut.fit.sp1.api.data.model.media.Media
import org.mapstruct.*

@Mapper(
    componentModel = "spring",
    unmappedTargetPolicy = ReportingPolicy.IGNORE,
    uses = [TagMapper::class]
)
abstract class MediaMapper {

    @Mapping(target = "createdById", source = "createdBy.id")
    @Mapping(target = "createdBy", ignore = true)
    abstract fun toDto(media: Media?): MediaDto?

    abstract fun toEntity(mediaDto: MediaDto?): Media?

    @AfterMapping
    fun enrichWithLikes(@MappingTarget mediaDto: MediaDto, media: Media?) {
        mediaDto.likes = media?.likedBy?.size ?: 0
    }
}

package cz.cvut.fit.sp1.api.component.mapper

import cz.cvut.fit.sp1.api.data.dto.MediaDto
import cz.cvut.fit.sp1.api.data.model.media.Media
import org.mapstruct.AfterMapping
import org.mapstruct.Mapper
import org.mapstruct.Mapping
import org.mapstruct.MappingTarget
import org.mapstruct.ReportingPolicy

@Mapper(
    componentModel = "spring",
    unmappedTargetPolicy = ReportingPolicy.IGNORE,
    uses = [TagMapper::class, UserAccountMapper::class]
)
abstract class MediaMapper {

    @Mapping(target = "createdById", source = "createdBy.id")
    abstract fun toDto(media: Media?): MediaDto?

    abstract fun toEntity(mediaDto: MediaDto?): Media?

    @AfterMapping
    fun enrichWithLikes(@MappingTarget mediaDto: MediaDto, media: Media?) {
        mediaDto.likes = media?.likedBy?.size ?: 0
    }
}

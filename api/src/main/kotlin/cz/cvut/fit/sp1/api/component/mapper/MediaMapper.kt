package cz.cvut.fit.sp1.api.component.mapper

import cz.cvut.fit.sp1.api.data.dto.MediaDto
import cz.cvut.fit.sp1.api.data.model.media.Media
import org.mapstruct.Mapper

@Mapper(componentModel = "spring")
interface MediaMapper {
    fun toDto(media: Media): MediaDto

    fun toEntity(mediaDto: MediaDto): Media
}

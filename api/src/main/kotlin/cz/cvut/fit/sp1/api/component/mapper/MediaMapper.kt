package cz.cvut.fit.sp1.api.component.mapper

import cz.cvut.fit.sp1.api.data.dto.MediaDto
import cz.cvut.fit.sp1.api.data.model.media.Media
import org.mapstruct.Mapper
import org.mapstruct.ReportingPolicy

@Mapper(componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        uses = [TagMapper::class, UserAccountMapper::class])
interface MediaMapper {

    fun toDto(media: Media): MediaDto

    fun toEntity(mediaDto: MediaDto): Media
}

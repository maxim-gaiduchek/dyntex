package cz.cvut.fit.sp1.api.component.mapper

import cz.cvut.fit.sp1.api.data.dto.VideoDto
import cz.cvut.fit.sp1.api.data.model.media.Video
import org.mapstruct.Mapper
import org.mapstruct.ReportingPolicy

@Mapper(componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        uses = [UserAccountMapper::class, TagMapper::class])
interface VideoMapper {

    fun toDto(video: Video): VideoDto

    fun toEntity(videoDto: VideoDto): Video
}

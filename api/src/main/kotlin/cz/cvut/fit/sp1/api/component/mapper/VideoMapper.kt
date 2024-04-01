package cz.cvut.fit.sp1.api.component.mapper

import cz.cvut.fit.sp1.api.data.dto.VideoDto
import cz.cvut.fit.sp1.api.data.model.media.Video
import org.mapstruct.InheritInverseConfiguration
import org.mapstruct.Mapper

@Mapper(componentModel = "spring")
interface VideoMapper {
    @InheritInverseConfiguration
    fun toDto(video: Video): VideoDto

    fun toBean(videoDto: VideoDto): Video
}

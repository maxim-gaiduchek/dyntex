package cz.cvut.fit.sp1.api.data.service.interfaces

import cz.cvut.fit.sp1.api.data.dto.VideoDto
import cz.cvut.fit.sp1.api.data.dto.search.SearchMediaParamsDto
import cz.cvut.fit.sp1.api.data.dto.search.SearchVideoDto
import cz.cvut.fit.sp1.api.data.model.media.Video
import org.springframework.web.multipart.MultipartFile
import java.util.*

interface VideoService {
    fun findById(id: Long): Optional<Video>

    fun getByIdOrThrow(id: Long): Video

    fun findAll(paramsDto: SearchMediaParamsDto<Video>?): SearchVideoDto?

    fun create(
        video: MultipartFile,
        videoDto: VideoDto,
    ): Video
}

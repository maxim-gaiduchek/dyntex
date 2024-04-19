package cz.cvut.fit.sp1.api.data.service.interfaces

import cz.cvut.fit.sp1.api.data.dto.VideoDto
import cz.cvut.fit.sp1.api.data.model.media.Video
import org.springframework.web.multipart.MultipartFile
import java.util.*

interface VideoService {

    fun getById(id: Long): Optional<Video>

    fun findByIdOrThrow(id: Long): Video

    fun create(video: MultipartFile): Video

    fun updateVideo(id: Long, videoDto: VideoDto): Video
}
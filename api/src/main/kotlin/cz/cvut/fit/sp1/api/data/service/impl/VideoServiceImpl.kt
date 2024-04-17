package cz.cvut.fit.sp1.api.data.service.impl

import cz.cvut.fit.sp1.api.component.FileStorage
import cz.cvut.fit.sp1.api.component.MediaProcessor
import cz.cvut.fit.sp1.api.data.model.media.Video
import cz.cvut.fit.sp1.api.data.repository.VideoRepository
import cz.cvut.fit.sp1.api.data.service.interfaces.VideoService
import cz.cvut.fit.sp1.api.exception.EntityNotFoundException
import cz.cvut.fit.sp1.api.exception.exceptioncodes.VideoExceptionCodes
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile
import java.util.*

@Service
class VideoServiceImpl(
    private val videoRepository: VideoRepository,
    private val fileStorage: FileStorage,
) : VideoService {

    override fun getById(id: Long): Optional<Video> {
        return videoRepository.findById(id)
    }

    override fun findByIdOrThrow(id: Long): Video {
        return getById(id)
            .orElseThrow { throw EntityNotFoundException(VideoExceptionCodes.VIDEO_NOT_FOUND) }
    }

    override fun create(video: MultipartFile): Video {
        val processor = MediaProcessor(video, fileStorage)
        val videoEntity = processor.extractVideoInfo()
        videoRepository.save(videoEntity)

        return videoEntity
    }
}

package cz.cvut.fit.sp1.api.data.service

import cz.cvut.fit.sp1.api.component.FileStorage
import cz.cvut.fit.sp1.api.component.MediaProcessor
import cz.cvut.fit.sp1.api.data.model.media.Video
import cz.cvut.fit.sp1.api.data.repository.VideoRepository
import cz.cvut.fit.sp1.api.exception.EntityNotFoundException
import cz.cvut.fit.sp1.api.exception.exceptioncodes.VideoExceptionCodes
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile

@Service
class VideoService(
    private val videoRepository: VideoRepository,
    private val fileStorage: FileStorage,
) {
    fun get(id: Long): Video {
        return videoRepository.findById(id).orElseThrow { throw EntityNotFoundException(VideoExceptionCodes.INVALID_VIDEO_ID) }
    }

    fun create(video: MultipartFile): Video {
        val processor = MediaProcessor(video, fileStorage)
        val videoEntity = processor.extractVideoInfo()
        videoRepository.save(videoEntity)

        return videoEntity
    }
}

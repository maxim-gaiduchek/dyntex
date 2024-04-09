package cz.cvut.fit.sp1.api.data.service

import cz.cvut.fit.sp1.api.component.FileStorage
import cz.cvut.fit.sp1.api.component.MediaProcessor
import cz.cvut.fit.sp1.api.data.model.media.Mask
import cz.cvut.fit.sp1.api.data.repository.MaskRepository
import cz.cvut.fit.sp1.api.exception.exceptioncodes.VideoExceptionCodes
import cz.cvut.fit.sp1.api.exception.mediaExceptions.video.VideoNotFoundException
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile

@Service
class MaskService (
    private val maskRepository: MaskRepository,
    private val fileStorage: FileStorage,
) {
    fun get(id: Long): Mask {
        return maskRepository.findById(id).orElseThrow { throw VideoNotFoundException(VideoExceptionCodes.INVALID_VIDEO_ID) }
    }

    fun create(mask: MultipartFile): Mask {
        val processor = MediaProcessor(mask, fileStorage)
        val maskEntity = processor.extractMaskInfo()
        maskRepository.save(maskEntity)

        return maskEntity
    }
}
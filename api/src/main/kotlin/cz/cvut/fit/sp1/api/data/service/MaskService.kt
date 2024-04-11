package cz.cvut.fit.sp1.api.data.service

import cz.cvut.fit.sp1.api.component.FileStorage
import cz.cvut.fit.sp1.api.component.MediaProcessor
import cz.cvut.fit.sp1.api.data.model.media.Mask
import cz.cvut.fit.sp1.api.data.repository.MaskRepository
import cz.cvut.fit.sp1.api.exception.EntityNotFoundException
import cz.cvut.fit.sp1.api.exception.exceptioncodes.MaskExceptionCodes
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile

@Service
class MaskService (
    private val maskRepository: MaskRepository,
    private val fileStorage: FileStorage,
) {
    fun get(id: Long): Mask {
        return maskRepository.findById(id).orElseThrow { throw EntityNotFoundException(MaskExceptionCodes.MASK_DOES_NOT_EXIST) }
    }

    fun create(mask: MultipartFile): Mask {
        val processor = MediaProcessor(mask, fileStorage)
        val maskEntity = processor.extractMaskInfo()
        maskRepository.save(maskEntity)

        return maskEntity
    }
}
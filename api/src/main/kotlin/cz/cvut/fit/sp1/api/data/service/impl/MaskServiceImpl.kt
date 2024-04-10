package cz.cvut.fit.sp1.api.data.service.impl

import cz.cvut.fit.sp1.api.data.model.media.Mask
import cz.cvut.fit.sp1.api.data.repository.MaskRepository
import cz.cvut.fit.sp1.api.data.service.interfaces.MaskService
import cz.cvut.fit.sp1.api.exception.EntityNotFoundException
import cz.cvut.fit.sp1.api.exception.exceptioncodes.MaskExceptionCodes
import org.springframework.stereotype.Service
import java.util.*

@Service
class MaskServiceImpl(
        private val maskRepository: MaskRepository,
) : MaskService {

    override fun findById(id: Long): Optional<Mask> {
        return maskRepository.findById(id)
    }

    override fun getByIdOrThrow(id: Long): Mask {
        return findById(id).orElseThrow {
            throw EntityNotFoundException(MaskExceptionCodes.MASK_NOT_FOUND, id)
        }
    }
}

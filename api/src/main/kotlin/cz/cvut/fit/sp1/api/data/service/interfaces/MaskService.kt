package cz.cvut.fit.sp1.api.data.service.interfaces

import cz.cvut.fit.sp1.api.data.model.media.Mask
import org.springframework.web.multipart.MultipartFile
import java.util.*

interface MaskService {

    fun getById(id: Long): Optional<Mask>

    fun findByIdOrThrow(id: Long): Mask

    fun create(mask: MultipartFile): Mask
}
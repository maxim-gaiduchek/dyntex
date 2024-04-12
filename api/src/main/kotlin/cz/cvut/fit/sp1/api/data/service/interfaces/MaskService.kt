package cz.cvut.fit.sp1.api.data.service.interfaces

import cz.cvut.fit.sp1.api.data.model.media.Mask
import org.springframework.web.multipart.MultipartFile
import java.util.*

interface MaskService {

    fun findById(id: Long): Optional<Mask>

    fun getByIdOrThrow(id: Long): Mask

    fun create(mask: MultipartFile): Mask
}
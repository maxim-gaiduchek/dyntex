package cz.cvut.fit.sp1.api.data.service.interfaces

import cz.cvut.fit.sp1.api.data.dto.MaskDto
import cz.cvut.fit.sp1.api.data.dto.search.SearchMaskDto
import cz.cvut.fit.sp1.api.data.dto.search.SearchMediaParamsDto
import cz.cvut.fit.sp1.api.data.model.media.Mask
import org.springframework.web.multipart.MultipartFile
import java.util.*

interface MaskService {

    fun findById(id: Long): Optional<Mask>

    fun getByIdOrThrow(id: Long): Mask

    fun findAll(paramsDto: SearchMediaParamsDto<Mask>?): SearchMaskDto?

    fun create(mask: MultipartFile, maskDto: MaskDto): Mask

    fun update(id: Long, maskDto: MaskDto): Mask

    fun delete(id: Long)

    fun countAll(): Long

    fun toggleLike(maskId: Long, userId: Long): Mask
}
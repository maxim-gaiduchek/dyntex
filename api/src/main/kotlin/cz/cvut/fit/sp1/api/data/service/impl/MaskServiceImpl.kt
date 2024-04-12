package cz.cvut.fit.sp1.api.data.service.impl

import cz.cvut.fit.sp1.api.component.FileStorage
import cz.cvut.fit.sp1.api.component.MediaProcessor
import cz.cvut.fit.sp1.api.component.mapper.MaskMapper
import cz.cvut.fit.sp1.api.data.dto.search.SearchMaskDto
import cz.cvut.fit.sp1.api.data.dto.search.SearchMediaParamsDto
import cz.cvut.fit.sp1.api.data.model.media.Mask
import cz.cvut.fit.sp1.api.data.repository.MaskRepository
import cz.cvut.fit.sp1.api.data.service.interfaces.MaskService
import cz.cvut.fit.sp1.api.exception.EntityNotFoundException
import cz.cvut.fit.sp1.api.exception.exceptioncodes.MaskExceptionCodes
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile
import java.util.*

@Service
class MaskServiceImpl(
        private val maskRepository: MaskRepository,
        private val fileStorage: FileStorage,
        private val maskMapper: MaskMapper
) : MaskService {

    override fun findById(id: Long): Optional<Mask> {
        return maskRepository.findById(id)
    }

    override fun getByIdOrThrow(id: Long): Mask {
        return findById(id)
                .orElseThrow { throw EntityNotFoundException(MaskExceptionCodes.MASK_NOT_FOUND, id) }
    }

    override fun findAll(paramsDto: SearchMediaParamsDto<Mask>?): SearchMaskDto? {
        if (paramsDto == null) {
            return null
        }
        val specification = paramsDto.buildSpecification()
        val pageable = paramsDto.buildPageable()
        val page = maskRepository.findAll(specification, pageable)
        val masks = page.content.stream()
                .map { maskMapper.toDto(it)!! }
                .toList()
        return SearchMaskDto(
                masks = masks,
                currentPage = page.number - 1,
                totalPages = page.totalPages,
                totalMatches = page.totalElements
        )
    }

    override fun create(mask: MultipartFile): Mask {
        val processor = MediaProcessor(mask, fileStorage)
        val maskEntity = processor.extractMaskInfo()
        maskRepository.save(maskEntity)
        return maskEntity
    }
}
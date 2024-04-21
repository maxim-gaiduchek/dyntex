package cz.cvut.fit.sp1.api.data.service.impl

import cz.cvut.fit.sp1.api.component.mapper.TagMapper
import cz.cvut.fit.sp1.api.data.dto.TagDto
import cz.cvut.fit.sp1.api.data.dto.search.SearchTagDto
import cz.cvut.fit.sp1.api.data.dto.search.SearchTagParamsDto
import cz.cvut.fit.sp1.api.data.model.Tag
import cz.cvut.fit.sp1.api.data.repository.TagRepository
import cz.cvut.fit.sp1.api.data.service.interfaces.TagService
import cz.cvut.fit.sp1.api.data.service.interfaces.UserAccountService
import cz.cvut.fit.sp1.api.exception.EntityNotFoundException
import cz.cvut.fit.sp1.api.exception.ValidationException
import cz.cvut.fit.sp1.api.exception.exceptioncodes.TagExceptionCodes
import cz.cvut.fit.sp1.api.exception.exceptioncodes.ValidationExceptionCodes
import org.springframework.stereotype.Service
import java.util.*

@Service
class TagServiceImpl(
    private val tagRepository: TagRepository,
    private val tagMapper: TagMapper,
    private val userAccountService: UserAccountService,
) : TagService {

    override fun findById(id: Long): Optional<Tag> {
        return tagRepository.findById(id)
    }

    override fun getByIdOrThrow(id: Long): Tag {
        return findById(id)
            .orElseThrow { EntityNotFoundException(TagExceptionCodes.TAG_NOT_FOUND, id) }
    }

    override fun findAll(paramsDto: SearchTagParamsDto?): SearchTagDto? {
        if (paramsDto == null) {
            return null
        }
        val specification = paramsDto.buildSpecification()
        val pageable = paramsDto.buildPageable()
        val page = tagRepository.findAll(specification, pageable)
        val tags =
            page.content.stream()
                .map { tagMapper.toDto(it)!! }
                .toList()
        return SearchTagDto(
            tags = tags,
            currentPage = page.number + 1,
            totalPages = page.totalPages,
            totalMatches = page.totalElements,
        )
    }

    override fun getAllByIds(ids: List<Long>): MutableList<Tag> {
        val tags = tagRepository.findAllById(ids)
        val tagIds = tags.map { it.id }
        ids.forEach {
            if (!tagIds.contains(it)) {
                throw EntityNotFoundException(TagExceptionCodes.TAG_NOT_FOUND, it)
            }
        }
        return tags
    }

    override fun create(tagDto: TagDto): Tag {
        if (tagRepository.existsByName(tagDto.name!!)) {
            throw ValidationException(TagExceptionCodes.TAG_NAME_EXISTS, tagDto.name)
        }
        val tag =
            tagMapper.toEntity(tagDto) ?: throw ValidationException(
                ValidationExceptionCodes.INVALID_DTO,
            )
        enrichWithModels(tag)
        return tagRepository.save(tag)
    }

    private fun enrichWithModels(tag: Tag) {
        val user = userAccountService.getByAuthentication()
        tag.createdBy = user
        user.createdTags.add(tag)
    }

    override fun delete(id: Long, forceDelete: Boolean) {
        val tag = getByIdOrThrow(id)
        if (!forceDelete && tag.media.isNotEmpty()) {
            throw ValidationException(TagExceptionCodes.TAG_IS_USED, tag.id)
        }
        tagRepository.delete(tag)
    }
}
package cz.cvut.fit.sp1.api.data.service.impl

import cz.cvut.fit.sp1.api.component.mapper.TagMapper
import cz.cvut.fit.sp1.api.data.dto.TagDto
import cz.cvut.fit.sp1.api.data.model.Tag
import cz.cvut.fit.sp1.api.data.repository.TagRepository
import cz.cvut.fit.sp1.api.data.service.interfaces.TagService
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
) : TagService {

    override fun findById(id: Long): Optional<Tag> {
        return tagRepository.findById(id)
    }

    override fun getByIdOrThrow(id: Long): Tag {
        return findById(id)
            .orElseThrow { EntityNotFoundException(TagExceptionCodes.TAG_NOT_FOUND, id) }
    }

    override fun findAll(): List<Tag> {
        return tagRepository.findAll()
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
        val tagEntity =
            tagMapper.toEntity(tagDto) ?: throw ValidationException(
                ValidationExceptionCodes.INVALID_DTO,
            )
        return tagRepository.save(tagEntity)
    }
}

package cz.cvut.fit.sp1.api.data.service.impl

import cz.cvut.fit.sp1.api.component.mapper.TagMapper
import cz.cvut.fit.sp1.api.data.dto.TagDto
import cz.cvut.fit.sp1.api.data.model.Tag
import cz.cvut.fit.sp1.api.data.repository.TagsRepository
import cz.cvut.fit.sp1.api.exception.EntityNotFoundException
import cz.cvut.fit.sp1.api.exception.ValidationException
import cz.cvut.fit.sp1.api.exception.exceptioncodes.TagExceptionCodes
import cz.cvut.fit.sp1.api.exception.exceptioncodes.ValidationExceptionCodes
import org.springframework.stereotype.Service
import kotlin.jvm.optionals.getOrElse

@Service
class TagsService(
    private val tagsRepository: TagsRepository,
    private val tagMapper: TagMapper,
) {
    fun create(tagDto: TagDto): Tag {
        val tagEntity =
            tagMapper.toEntity(tagDto) ?: throw ValidationException(
                ValidationExceptionCodes.INVALID_DTO,
            )
        return tagsRepository.save(tagEntity)
    }

    fun get(id: Long): Tag {
        return find(id) ?: throw EntityNotFoundException(
            TagExceptionCodes.TAG_NOT_FOUND,
            id,
        )
    }

    fun save(tag: Tag): Tag {
        return tagsRepository.save(tag)
    }

    fun find(id: Long): Tag? {
        return tagsRepository.findById(id).getOrElse { null }
    }

    fun getAll(): List<Tag> {
        return tagsRepository.findAll()
    }
}

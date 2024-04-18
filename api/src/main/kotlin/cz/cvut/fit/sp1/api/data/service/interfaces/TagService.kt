package cz.cvut.fit.sp1.api.data.service.interfaces

import cz.cvut.fit.sp1.api.data.dto.TagDto
import cz.cvut.fit.sp1.api.data.model.Tag
import java.util.*

interface TagService {

    fun findById(id: Long): Optional<Tag>

    fun getByIdOrThrow(id: Long): Tag

    fun findAll(): List<Tag>

    fun getAllByIds(ids: List<Long>): MutableList<Tag>

    fun create(tagDto: TagDto): Tag
}
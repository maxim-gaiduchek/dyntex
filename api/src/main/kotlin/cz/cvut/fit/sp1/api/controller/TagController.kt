package cz.cvut.fit.sp1.api.controller

import cz.cvut.fit.sp1.api.component.mapper.TagMapper
import cz.cvut.fit.sp1.api.data.dto.TagDto
import cz.cvut.fit.sp1.api.data.service.interfaces.TagService
import cz.cvut.fit.sp1.api.validation.group.CreateGroup
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/tags")
class TagController(
    private val tagsService: TagService,
    private val tagMapper: TagMapper,
) {
    @GetMapping("/{id}")
    fun getById(
        @PathVariable id: Long,
    ): TagDto? {
        val tag = tagsService.getByIdOrThrow(id)
        return tagMapper.toDto(tag)
    }

    @GetMapping
    fun findAll(): List<TagDto?> {
        val tags = tagsService.findAll()
        return tagMapper.toDtos(tags)
    }

    @PostMapping
    fun create(
        @Validated(CreateGroup::class) @RequestBody tagDto: TagDto,
    ): TagDto? {
        val tag = tagsService.create(tagDto)
        return tagMapper.toDto(tag)
    }
}

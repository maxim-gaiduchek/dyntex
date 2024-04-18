package cz.cvut.fit.sp1.api.controller

import cz.cvut.fit.sp1.api.component.mapper.TagMapper
import cz.cvut.fit.sp1.api.data.dto.TagDto
import cz.cvut.fit.sp1.api.data.service.impl.TagsService
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/tags")
class TagsController(
    private val tagsService: TagsService,
    private val tagMapper: TagMapper,
) {
    @GetMapping("/{id}")
    fun findById(
        @PathVariable id: Long,
    ): TagDto? {
        val tag = tagsService.get(id)
        return tagMapper.toDto(tag)
    }

    @GetMapping
    fun getALl(): List<TagDto> {
        return tagsService.getAll().mapNotNull { tagMapper.toDto(it) }
    }

    @PostMapping
    fun create(
        @RequestBody tagDto: TagDto,
    ): TagDto? {
        return tagMapper.toDto(tagsService.create(tagDto))
    }
}

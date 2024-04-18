package cz.cvut.fit.sp1.api.controller

import cz.cvut.fit.sp1.api.component.mapper.TagMapper
import cz.cvut.fit.sp1.api.data.dto.TagDto
import cz.cvut.fit.sp1.api.data.dto.search.SearchTagDto
import cz.cvut.fit.sp1.api.data.dto.search.SearchTagParamsDto
import cz.cvut.fit.sp1.api.data.service.interfaces.TagService
import cz.cvut.fit.sp1.api.validation.group.CreateGroup
import jakarta.annotation.security.RolesAllowed
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

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
    fun findAll(
        @RequestBody paramsDto: SearchTagParamsDto?,
    ): SearchTagDto? {
        return tagsService.findAll(paramsDto)
    }

    @PostMapping
    @RolesAllowed("ADMIN")
    fun create(
        @Validated(CreateGroup::class) @RequestBody tagDto: TagDto,
    ): TagDto? {
        val tag = tagsService.create(tagDto)
        return tagMapper.toDto(tag)
    }
}

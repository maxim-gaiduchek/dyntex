package cz.cvut.fit.sp1.api.controller

import cz.cvut.fit.sp1.api.component.mapper.TagMapper
import cz.cvut.fit.sp1.api.data.dto.TagDto
import cz.cvut.fit.sp1.api.data.dto.search.SearchTagDto
import cz.cvut.fit.sp1.api.data.dto.search.SearchTagParamsDto
import cz.cvut.fit.sp1.api.data.service.interfaces.TagService
import cz.cvut.fit.sp1.api.validation.group.CreateGroup
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.access.annotation.Secured
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.ModelAttribute
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/tags")
class TagController(
    private val tagService: TagService,
    private val tagMapper: TagMapper,
) {
    @GetMapping("/{id}")
    fun getById(
        @PathVariable id: Long,
    ): TagDto? {
        val tag = tagService.getByIdOrThrow(id)
        return tagMapper.toDto(tag)
    }

    @GetMapping
    fun findAll(
        @ModelAttribute paramsDto: SearchTagParamsDto?,
    ): SearchTagDto? {
        return tagService.findAll(paramsDto)
    }

    @PostMapping
    @Secured("ADMIN")
    fun create(
        @Validated(CreateGroup::class) @RequestBody tagDto: TagDto,
    ): TagDto? {
        val tag = tagService.create(tagDto)
        return tagMapper.toDto(tag)
    }

    @DeleteMapping("/{id}")
    @Secured("ADMIN")
    fun delete(
        @PathVariable id: Long,
        @RequestParam("force", defaultValue = "false") forceDelete: Boolean
    ): ResponseEntity<Any> {
        tagService.delete(id, forceDelete)
        return ResponseEntity(HttpStatus.OK)
    }
}

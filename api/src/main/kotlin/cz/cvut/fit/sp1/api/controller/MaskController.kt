package cz.cvut.fit.sp1.api.controller

import cz.cvut.fit.sp1.api.component.FileStorage
import cz.cvut.fit.sp1.api.component.mapper.MaskMapper
import cz.cvut.fit.sp1.api.configuration.StoragePathProperties
import cz.cvut.fit.sp1.api.data.dto.MaskDto
import cz.cvut.fit.sp1.api.data.dto.MediaDto
import cz.cvut.fit.sp1.api.data.dto.VideoDto
import cz.cvut.fit.sp1.api.data.dto.search.SearchMaskDto
import cz.cvut.fit.sp1.api.data.dto.search.SearchMediaParamsDto
import cz.cvut.fit.sp1.api.data.model.media.Mask
import cz.cvut.fit.sp1.api.data.service.interfaces.MaskService
import cz.cvut.fit.sp1.api.validation.group.CreateGroup
import org.springframework.core.io.Resource
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.security.access.annotation.Secured
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile
import kotlin.io.path.Path

@RestController
@RequestMapping("/masks")
class MaskController(
    private val maskService: MaskService,
    private val maskMapper: MaskMapper,
    private val storagePathProperties: StoragePathProperties,
    private val storage: FileStorage,
) {

    @GetMapping("/{id}")
    fun findById(@PathVariable id: Long): MaskDto? {
        val mask = maskService.getByIdOrThrow(id)
        return maskMapper.toDto(mask)
    }

    @GetMapping
    fun findAll(@ModelAttribute paramsDto: SearchMediaParamsDto<Mask>?): SearchMaskDto? {
        return maskService.findAll(paramsDto)
    }

    @GetMapping("/download/{maskName}")
    fun download(
        @PathVariable maskName: String,
    ): ResponseEntity<ByteArray> {
        val imageData = storage.readFileAsBytes(Path(storagePathProperties.mediaPath, maskName).toString())
        val headers = HttpHeaders()
        headers.contentType = MediaType.IMAGE_PNG
        return ResponseEntity(imageData, headers, HttpStatus.OK)
    }

    @PostMapping(consumes = [MediaType.MULTIPART_FORM_DATA_VALUE])
    @Secured("USER", "ADMIN")
    fun upload(
        @RequestParam("mask") maskFile: MultipartFile,
        @Validated(CreateGroup::class) @ModelAttribute maskDto: MaskDto,
    ): MaskDto? {
        val mask = maskService.create(maskFile, maskDto)
        return maskMapper.toDto(mask)
    }

    @DeleteMapping("/{id}")
    @Secured("USER", "ADMIN")
    fun delete(
        @PathVariable id: Long,
    ): ResponseEntity<Any> {
        maskService.delete(id)
        return ResponseEntity(HttpStatus.OK)
    }

    @PutMapping("/{id}")
    fun updateMask(@PathVariable id: Long, @RequestBody maskDto: MaskDto): MaskDto? {
        val mask = maskService.update(id, maskDto)
        return maskMapper.toDto(mask)
    }
}
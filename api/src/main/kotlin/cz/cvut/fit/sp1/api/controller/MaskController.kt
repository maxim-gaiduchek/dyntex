package cz.cvut.fit.sp1.api.controller

import cz.cvut.fit.sp1.api.component.mapper.MaskMapper
import cz.cvut.fit.sp1.api.data.dto.MaskDto
import cz.cvut.fit.sp1.api.data.dto.search.SearchMaskDto
import cz.cvut.fit.sp1.api.data.dto.search.SearchMediaParamsDto
import cz.cvut.fit.sp1.api.data.model.media.Mask
import cz.cvut.fit.sp1.api.data.service.interfaces.MaskService
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile

@RestController
@CrossOrigin(origins = ["*"])
@RequestMapping("/masks")
class MaskController(
        private val maskService: MaskService,
        private val maskMapper: MaskMapper
) {

    @GetMapping("/{id}")
    fun findById(@PathVariable id: Long): MaskDto? {
        val mask = maskService.getByIdOrThrow(id)
        return maskMapper.toDto(mask)
    }

    @GetMapping
    fun findAll(@RequestBody paramsDto: SearchMediaParamsDto<Mask>?): SearchMaskDto? {
        return maskService.findAll(paramsDto)
    }

    @PostMapping(consumes = [MediaType.MULTIPART_FORM_DATA_VALUE])
    fun upload(
            // TODO Get DTO with Media file
            @RequestParam("mask") mask: MultipartFile,
    ) {
        // TODO implement input validation
        maskService.create(mask) // TODO return DTO
    }
}
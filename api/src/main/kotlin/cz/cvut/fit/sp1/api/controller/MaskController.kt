package cz.cvut.fit.sp1.api.controller

import cz.cvut.fit.sp1.api.component.mapper.MaskMapper
import cz.cvut.fit.sp1.api.data.dto.MaskDto
import cz.cvut.fit.sp1.api.data.service.interfaces.MaskService
import org.springframework.web.bind.annotation.*

@RestController
@CrossOrigin(origins = ["*"])
@RequestMapping("/masks")
class MaskController(
        private val maskService: MaskService,
        private val maskMapper: MaskMapper
) {

    @GetMapping("/{id}")
    fun findById(@PathVariable id: Long): MaskDto {
        val mask = maskService.getByIdOrThrow(id)
        return maskMapper.toDto(mask)
    }
}

package cz.cvut.fit.sp1.api.controller

import cz.cvut.fit.sp1.api.data.service.MaskService
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile

@RestController
@CrossOrigin(origins = ["*"])
@RequestMapping(
    value = ["/mask"],
)
class MaskController(
    private val maskService: MaskService
) {
    @PostMapping(consumes = [MediaType.MULTIPART_FORM_DATA_VALUE])
    fun upload(
        @RequestParam("mask") mask: MultipartFile,
    ) {
        // TODO implement input validation
        maskService.create(mask)
    }
}
package cz.cvut.fit.sp1.api.controller

import cz.cvut.fit.sp1.api.data.service.interfaces.MaskService
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.multipart.MultipartFile

@RestController
@CrossOrigin(origins = ["*"])
@RequestMapping("/masks")
class MaskController(
    private val maskService: MaskService
) {
    @PostMapping(consumes = [MediaType.MULTIPART_FORM_DATA_VALUE])
    fun upload(
        // TODO Get DTO with Media file
        @RequestParam("mask") mask: MultipartFile,
    ) {
        // TODO implement input validation
        maskService.create(mask) // TODO return DTO
    }
}
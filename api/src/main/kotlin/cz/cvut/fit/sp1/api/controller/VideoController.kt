package cz.cvut.fit.sp1.api.controller

import cz.cvut.fit.sp1.api.data.service.VideoService
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.multipart.MultipartFile

@RestController
@CrossOrigin(origins = ["*"])
@RequestMapping(
    value = ["/videos"],
)
class VideoController(
    private val videoService: VideoService,
) {
    @PostMapping(consumes = [MediaType.MULTIPART_FORM_DATA_VALUE])
    fun upload(
        @RequestParam("video") video: MultipartFile,
    ) {
        // TODO implement input validation
        videoService.create(video)
    }
}

package cz.cvut.fit.sp1.api.controller

import cz.cvut.fit.sp1.api.data.dto.VideoDto
import cz.cvut.fit.sp1.api.data.service.interfaces.VideoService
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile

@RestController
@CrossOrigin(origins = ["*"])
@RequestMapping("/videos")
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

    @PutMapping("/api/video/{id}")
    fun updateVideo(@PathVariable id: Long, @RequestBody videoDto: VideoDto): ResponseEntity<Any> {
        // Call service layer to update the video
        videoService.updateVideo(id, videoDto)
        // Return appropriate response entity
        return ResponseEntity.ok().build();
    }

}

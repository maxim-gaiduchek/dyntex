package cz.cvut.fit.sp1.api.controller

import cz.cvut.fit.sp1.api.data.service.interfaces.VideoService
import cz.cvut.fit.sp1.api.component.mapper.VideoMapper
import cz.cvut.fit.sp1.api.data.dto.search.SearchMediaParamsDto
import cz.cvut.fit.sp1.api.data.dto.search.SearchVideoDto
import cz.cvut.fit.sp1.api.data.dto.VideoDto
import cz.cvut.fit.sp1.api.data.service.interfaces.VideoService
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile

@RestController
@CrossOrigin(origins = ["*"])
@RequestMapping("/videos")
class VideoController(
        private val videoService: VideoService,
        private val videoMapper: VideoMapper
) {

    @GetMapping("/{id}")
    fun findById(@PathVariable id: Long): VideoDto {
        val video = videoService.getByIdOrThrow(id)
        return videoMapper.toDto(video)
    }

    /*@GetMapping
    fun find(@RequestBody paramsDto: SearchMediaParamsDto): SearchVideoDto {
        return videoService.findAll(paramsDto)
    }*/

    @PostMapping(consumes = [MediaType.MULTIPART_FORM_DATA_VALUE])
    fun upload(
            @RequestParam("video") video: MultipartFile,
    ) {
        // TODO implement input validation
        videoService.create(video)
    }
}

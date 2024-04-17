package cz.cvut.fit.sp1.api.controller

import cz.cvut.fit.sp1.api.component.mapper.VideoMapper
import cz.cvut.fit.sp1.api.data.dto.VideoDto
import cz.cvut.fit.sp1.api.data.dto.VideoDtoRequest
import cz.cvut.fit.sp1.api.data.dto.search.SearchMediaParamsDto
import cz.cvut.fit.sp1.api.data.dto.search.SearchVideoDto
import cz.cvut.fit.sp1.api.data.model.media.Video
import cz.cvut.fit.sp1.api.data.service.interfaces.VideoService
import jakarta.annotation.security.RolesAllowed
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile

@RestController
@RequestMapping("/videos")
class VideoController(
    private val videoService: VideoService,
    private val videoMapper: VideoMapper,
) {
    @GetMapping("/{id}")
    fun findById(
        @PathVariable id: Long,
    ): VideoDto? {
        val video = videoService.getByIdOrThrow(id)
        return videoMapper.toDto(video)
    }

    @GetMapping
    fun findAll(
        @ModelAttribute paramsDto: SearchMediaParamsDto<Video>?,
    ): SearchVideoDto? {
        return videoService.findAll(paramsDto)
    }

    @PostMapping
    @RolesAllowed("USER", "ADMIN")
    fun upload(
        @RequestParam("video") videoFile: MultipartFile,
        @ModelAttribute videoDto: VideoDtoRequest,
    ): VideoDto? {
        val video = videoService.create(videoFile, videoDto)
        return videoMapper.toDto(video)
    }
}

package cz.cvut.fit.sp1.api.controller

import cz.cvut.fit.sp1.api.component.FileStorage
import cz.cvut.fit.sp1.api.component.mapper.VideoMapper
import cz.cvut.fit.sp1.api.configuration.StoragePathProperties
import cz.cvut.fit.sp1.api.data.dto.VideoDto
import cz.cvut.fit.sp1.api.data.dto.VideoDtoRequest
import cz.cvut.fit.sp1.api.data.dto.search.SearchMediaParamsDto
import cz.cvut.fit.sp1.api.data.dto.search.SearchVideoDto
import cz.cvut.fit.sp1.api.data.model.media.Video
import cz.cvut.fit.sp1.api.data.service.interfaces.VideoService
import jakarta.annotation.security.RolesAllowed
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile
import java.net.http.HttpHeaders

@RestController
@RequestMapping("/videos")
class VideoController(
    private val videoService: VideoService,
    private val videoMapper: VideoMapper,
    private val storagePathProperties: StoragePathProperties,
    private val storage: FileStorage,
) {
    @GetMapping("/{id}")
    fun findById(
        @PathVariable id: Long,
    ): VideoDto? {
        val video = videoService.getByIdOrThrow(id)
        return videoMapper.toDto(video)
    }

    @GetMapping("/previews/{previewName}")
    fun getPreview(
        @PathVariable previewName: String,
    ): ResponseEntity<ByteArray> {
        val imageData = storage.readFile(storagePathProperties.mediaPath + "/$previewName")
        val headers = org.springframework.http.HttpHeaders()
        headers.contentType = MediaType.IMAGE_PNG
        return ResponseEntity(imageData, headers, HttpStatus.OK)
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

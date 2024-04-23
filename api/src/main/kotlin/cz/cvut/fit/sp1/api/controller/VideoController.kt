package cz.cvut.fit.sp1.api.controller

import cz.cvut.fit.sp1.api.component.FileStorage
import cz.cvut.fit.sp1.api.component.mapper.VideoMapper
import cz.cvut.fit.sp1.api.configuration.StoragePathProperties
import cz.cvut.fit.sp1.api.data.dto.VideoDto
import cz.cvut.fit.sp1.api.data.dto.search.SearchMediaParamsDto
import cz.cvut.fit.sp1.api.data.dto.search.SearchVideoDto
import cz.cvut.fit.sp1.api.data.model.media.Video
import cz.cvut.fit.sp1.api.data.service.interfaces.VideoService
import cz.cvut.fit.sp1.api.validation.group.CreateGroup
import org.springframework.core.io.Resource
import org.springframework.core.io.support.ResourceRegion
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.security.access.annotation.Secured
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile

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

    @GetMapping("/stream/{videoName}")
    fun streamVideo(
        @PathVariable videoName: String,
        @RequestHeader headers: HttpHeaders,
    ): ResponseEntity<ResourceRegion> {
        val videoPath = "${storagePathProperties.mediaPath}/$videoName"
        val headersRange = headers["Range"]?.firstOrNull()
        val resourceRegion = storage.getResourceRegion(videoPath, headersRange)

        return ResponseEntity.status(HttpStatus.PARTIAL_CONTENT)
            .contentType(MediaType.parseMediaType("video/mp4"))
            .body(resourceRegion)
    }

    @GetMapping("/download/{videoName}")
    fun download(
        @PathVariable videoName: String,
    ): ResponseEntity<Resource> {
        val videoPath = storagePathProperties.mediaPath + "/$videoName"

        val headers =
            HttpHeaders().apply {
                contentType = MediaType.parseMediaType("video/mp4")
                add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"$videoName\"")
            }

        return ResponseEntity.ok()
            .headers(headers)
            .body(storage.readFileAsResource(videoPath))
    }

    @GetMapping("/previews/{previewName}")
    fun getPreview(
        @PathVariable previewName: String,
    ): ResponseEntity<ByteArray> {
        val imageData = storage.readFileAsBytes(storagePathProperties.mediaPath + "/$previewName")
        val headers = HttpHeaders()
        headers.contentType = MediaType.IMAGE_PNG
        return ResponseEntity(imageData, headers, HttpStatus.OK)
    }

    @GetMapping
    fun findAll(
        @ModelAttribute paramsDto: SearchMediaParamsDto<Video>?,
    ): SearchVideoDto? {
        return videoService.findAll(paramsDto)
    }

    @PostMapping(consumes = [MediaType.MULTIPART_FORM_DATA_VALUE])
    @Secured("USER", "ADMIN")
    fun upload(
        @RequestParam("video") videoFile: MultipartFile,
        @Validated(CreateGroup::class) @ModelAttribute videoDto: VideoDto,
    ): VideoDto? {
        val video = videoService.create(videoFile, videoDto)
        return videoMapper.toDto(video)
    }

    @DeleteMapping("/{id}")
    @Secured("USER", "ADMIN")
    fun delete(
        @PathVariable id: Long,
    ): ResponseEntity<Any> {
        videoService.delete(id)
        return ResponseEntity(HttpStatus.OK)
    }
}

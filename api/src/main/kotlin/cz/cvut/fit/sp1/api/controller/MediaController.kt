package cz.cvut.fit.sp1.api.controller

import cz.cvut.fit.sp1.api.component.FileStorage
import cz.cvut.fit.sp1.api.configuration.StoragePathProperties
import org.springframework.core.io.support.ResourceRegion
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import kotlin.io.path.Path

@RestController
@RequestMapping("/media")
class MediaController(
    private val storagePathProperties: StoragePathProperties,
    private val storage: FileStorage,
) {
    @GetMapping("/stream/{path}")
    fun streamVideo(
        @PathVariable path: String,
        @RequestHeader headers: HttpHeaders,
    ): ResponseEntity<ResourceRegion> {
        val mediaPath = "${storagePathProperties.mediaPath}/$path"
        val headersRange = headers["Range"]?.firstOrNull()
        val resourceRegion = storage.getResourceRegion(mediaPath, headersRange)

        return ResponseEntity.status(HttpStatus.PARTIAL_CONTENT)
            .contentType(MediaType.parseMediaType("video/mp4"))
            .body(resourceRegion)
    }

    @GetMapping("/previews/{path}")
    fun getPreview(
        @PathVariable path: String,
    ): ResponseEntity<ByteArray> {
        val imageData = storage.readFileAsBytes(Path(storagePathProperties.mediaPath, path).toString())
        val headers = HttpHeaders()
        headers.contentType = MediaType.IMAGE_PNG
        return ResponseEntity(imageData, headers, HttpStatus.OK)
    }
}

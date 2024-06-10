package cz.cvut.fit.sp1.api.controller

import cz.cvut.fit.sp1.api.component.FileStorage
import cz.cvut.fit.sp1.api.configuration.StoragePathProperties
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.Mockito.*
import org.springframework.core.io.ByteArrayResource
import org.springframework.core.io.Resource
import org.springframework.core.io.support.ResourceRegion
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.mock.web.MockHttpServletRequest
import org.springframework.mock.web.MockMultipartFile
import org.springframework.security.test.context.support.WithMockUser
import kotlin.io.path.Path
import org.junit.jupiter.api.Assertions.*

class MediaControllerTest {

    private lateinit var mediaController: MediaController
    private lateinit var storagePathProperties: StoragePathProperties
    private lateinit var storage: FileStorage

    @BeforeEach
    fun setUp() {
        storagePathProperties = StoragePathProperties()
        storagePathProperties.mediaPath = Path(System.getProperty("user.home"), "sp1", "storage").toString()
        storage = mock(FileStorage::class.java)
        mediaController = MediaController(storagePathProperties, storage)
    }

    @Test
    fun `test streamVideo`() {
        val videoPath = "test.mp4"
        val mediaPath = Path(storagePathProperties.mediaPath, videoPath).toString()
        val resourceRegion: ResourceRegion = mock(ResourceRegion::class.java)
        `when`(storage.getResourceRegion(mediaPath, "bytes=0-1024")).thenReturn(resourceRegion)

        val headers = HttpHeaders()
        headers.set("Range", "bytes=0-1024")

        val response = mediaController.streamVideo(videoPath, headers)

        // Debugging output to understand failure
        println("Response Status: ${response.statusCode}")
        println("Expected Status: ${HttpStatus.PARTIAL_CONTENT}")
        println("Response Content Type: ${response.headers.contentType}")
        println("Expected Content Type: ${MediaType.parseMediaType("video/mp4")}")
        println("Response Body: ${response.body}")
        println("Expected Body: ${resourceRegion}")

        assertEquals(HttpStatus.PARTIAL_CONTENT, response.statusCode)
        assertEquals(MediaType.parseMediaType("video/mp4"), response.headers.contentType)
        assertEquals(resourceRegion, response.body)
    }

    @Test
    fun `test getPreview`() {
        val previewPath = "preview.png"
        val mediaPath = Path(storagePathProperties.mediaPath, previewPath).toString()
        val imageData = ByteArray(1024)
        `when`(storage.readFileAsBytes(mediaPath)).thenReturn(imageData)

        val response = mediaController.getPreview(previewPath)

        assertEquals(HttpStatus.OK, response.statusCode)
        assertEquals(MediaType.IMAGE_PNG, response.headers.contentType)
        assertArrayEquals(imageData, response.body)
    }
}

package cz.cvut.fit.sp1.api.service
import cz.cvut.fit.sp1.api.component.FileStorage
import cz.cvut.fit.sp1.api.component.mapper.VideoMapper
import cz.cvut.fit.sp1.api.configuration.StoragePathProperties
import cz.cvut.fit.sp1.api.controller.VideoController
import cz.cvut.fit.sp1.api.data.dto.VideoDto
import cz.cvut.fit.sp1.api.data.dto.search.SearchVideoDto
import cz.cvut.fit.sp1.api.data.model.media.Video
import cz.cvut.fit.sp1.api.data.service.interfaces.VideoService
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.Mockito.*
import org.springframework.core.io.ByteArrayResource
import org.springframework.core.io.Resource
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.mock.web.MockMultipartFile
import org.springframework.security.test.context.support.WithMockUser
import kotlin.io.path.Path
import org.junit.jupiter.api.Assertions.*


class VideoControllerTest {

    private lateinit var videoController: VideoController
    private lateinit var videoService: VideoService
    private lateinit var videoMapper: VideoMapper
    private lateinit var storagePathProperties: StoragePathProperties
    private lateinit var storage: FileStorage

    @BeforeEach
    fun setUp() {
        videoService = mock(VideoService::class.java)
        videoMapper = mock(VideoMapper::class.java)
        storagePathProperties = StoragePathProperties()
        storagePathProperties.mediaPath = Path(System.getProperty("user.home"), "sp1", "storage").toString()
        storage = mock(FileStorage::class.java)
        videoController = VideoController(videoService, videoMapper, storagePathProperties, storage)
    }

    @Test
    fun `test findById`() {
        val video = Video("name", "path", "format")
        val videoDto = VideoDto(
            fps = 30L,
            duration = 120.0,
            id = 1L,
            name = "name",
            path = "path",
            previewPath = "previewPath",
            size = "size",
            description = "description",
            format = "format",
            createdBy = null,
            createdById = null,
            likes = 0,
            tags = mutableListOf(),
            tagIds = mutableListOf()
        )
        `when`(videoService.getByIdOrThrow(1L)).thenReturn(video)
        `when`(videoMapper.toDto(video)).thenReturn(videoDto)

        val result = videoController.findById(1L)

        assertEquals(videoDto, result)
    }

    @Test
    fun `test findAll`() {
        val searchVideoDto = SearchVideoDto(mutableListOf(), 1, 1, 1)
        `when`(videoService.findAll(null)).thenReturn(searchVideoDto)

        val result = videoController.findAll(null)

        assertEquals(searchVideoDto, result)
    }

    @Test
    fun `test download`() {
        val videoName = "test.mp4"
        val videoPath = Path(storagePathProperties.mediaPath, videoName).toString()
        val resource: Resource = ByteArrayResource("test content".toByteArray())
        `when`(storage.readFileAsResource(videoPath)).thenReturn(resource)

        val response = videoController.download(videoName)

        assertEquals(HttpStatus.OK, response.statusCode)
        assertEquals(MediaType.parseMediaType("video/mp4"), response.headers.contentType)
        assertEquals("attachment; filename=\"$videoName\"", response.headers[HttpHeaders.CONTENT_DISPOSITION]?.first())
        assertEquals(resource, response.body)
    }

    @Test
    @WithMockUser(roles = ["USER", "ADMIN"])
    fun `test upload`() {
        val videoFile = MockMultipartFile("video", "test.mp4", MediaType.MULTIPART_FORM_DATA_VALUE, "test content".toByteArray())
        val videoDto = VideoDto(
            fps = 30L,
            duration = 120.0,
            id = null,
            name = "name",
            path = "path",
            previewPath = "previewPath",
            size = "size",
            description = "description",
            format = "format",
            createdBy = null,
            createdById = null,
            likes = 0,
            tags = mutableListOf(),
            tagIds = mutableListOf(1L)
        )
        val createdVideo = Video("name", "path", "format")
        val createdVideoDto = VideoDto(
            fps = 30L,
            duration = 120.0,
            id = 1L,
            name = "name",
            path = "path",
            previewPath = "previewPath",
            size = "size",
            description = "description",
            format = "format",
            createdBy = null,
            createdById = null,
            likes = 0,
            tags = mutableListOf(),
            tagIds = mutableListOf(1L)
        )

        `when`(videoService.create(videoFile, videoDto)).thenReturn(createdVideo)
        `when`(videoMapper.toDto(createdVideo)).thenReturn(createdVideoDto)

        val result = videoController.upload(videoFile, videoDto)

        assertEquals(createdVideoDto, result)
    }

    @Test
    @WithMockUser(roles = ["USER", "ADMIN"])
    fun `test delete`() {
        val response = videoController.delete(1L)

        verify(videoService, times(1)).delete(1L)
        assertEquals(HttpStatus.OK, response.statusCode)
    }

    @Test
    fun `test updateVideo`() {
        val videoDto = VideoDto(
            fps = 30L,
            duration = 120.0,
            id = 1L,
            name = "updated name",
            path = "updated path",
            previewPath = "updated previewPath",
            size = "updated size",
            description = "updated description",
            format = "updated format",
            createdBy = null,
            createdById = null,
            likes = 0,
            tags = mutableListOf(),
            tagIds = mutableListOf(1L)
        )
        val updatedVideo = Video("updated name", "updated path", "updated format")
        val updatedVideoDto = VideoDto(
            fps = 30L,
            duration = 120.0,
            id = 1L,
            name = "updated name",
            path = "updated path",
            previewPath = "updated previewPath",
            size = "updated size",
            description = "updated description",
            format = "updated format",
            createdBy = null,
            createdById = null,
            likes = 0,
            tags = mutableListOf(),
            tagIds = mutableListOf(1L)
        )

        `when`(videoService.update(1L, videoDto)).thenReturn(updatedVideo)
        `when`(videoMapper.toDto(updatedVideo)).thenReturn(updatedVideoDto)

        val result = videoController.updateVideo(1L, videoDto)

        assertEquals(updatedVideoDto, result)
    }

    @Test
    fun `test toggleVideoLike`() {
        val updatedVideo = Video("name", "path", "format")
        val updatedVideoDto = VideoDto(
            fps = 30L,
            duration = 120.0,
            id = 1L,
            name = "name",
            path = "path",
            previewPath = "previewPath",
            size = "size",
            description = "description",
            format = "format",
            createdBy = null,
            createdById = null,
            likes = 1,
            tags = mutableListOf(),
            tagIds = mutableListOf(1L)
        )

        `when`(videoService.toggleLike(1L, 1L)).thenReturn(updatedVideo)
        `when`(videoMapper.toDto(updatedVideo)).thenReturn(updatedVideoDto)

        val result = videoController.toggleVideoLike(1L, 1L)

        assertEquals(updatedVideoDto, result)
    }
}

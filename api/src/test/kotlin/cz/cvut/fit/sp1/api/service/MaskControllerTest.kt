package cz.cvut.fit.sp1.api.service
import cz.cvut.fit.sp1.api.component.FileStorage
import cz.cvut.fit.sp1.api.component.mapper.MaskMapper
import cz.cvut.fit.sp1.api.configuration.StoragePathProperties
import cz.cvut.fit.sp1.api.controller.MaskController
import cz.cvut.fit.sp1.api.data.dto.MaskDto
import cz.cvut.fit.sp1.api.data.model.media.Mask
import cz.cvut.fit.sp1.api.data.service.interfaces.MaskService
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
import org.junit.jupiter.api.Assertions.*
import kotlin.io.path.Path


class MaskControllerTest {

    private lateinit var maskController: MaskController
    private lateinit var maskService: MaskService
    private lateinit var maskMapper: MaskMapper
    private lateinit var storagePathProperties: StoragePathProperties
    private lateinit var storage: FileStorage

    @BeforeEach
    fun setUp() {
        maskService = mock(MaskService::class.java)
        maskMapper = mock(MaskMapper::class.java)
        storagePathProperties = StoragePathProperties()
        storagePathProperties.mediaPath = Path(System.getProperty("user.home"), "sp1", "storage").toString()
        storage = mock(FileStorage::class.java)
        maskController = MaskController(maskService, maskMapper, storagePathProperties, storage)
    }

    @Test
    fun `test findById`() {
        val mask = Mask("name", "path", "format")
        val maskDto = MaskDto(
            id = 1L,
            name = "name",
            path = "path",
            size = "size",
            format = "format",
            createdBy = null,
            createdById = null,
            likes = 0,
            tags = mutableListOf(),
            tagIds = mutableListOf()
        )
        `when`(maskService.getByIdOrThrow(1L)).thenReturn(mask)
        `when`(maskMapper.toDto(mask)).thenReturn(maskDto)

        val result = maskController.findById(1L)

        assertEquals(maskDto, result)
    }

    @Test
    fun `test download`() {
        val maskName = "test.png"
        val maskPath = Path(storagePathProperties.mediaPath, maskName).toString()
        val resource: Resource = ByteArrayResource("test content".toByteArray())
        `when`(storage.readFileAsResource(maskPath)).thenReturn(resource)

        val response = maskController.download(maskName)

        assertEquals(HttpStatus.OK, response.statusCode)
        assertEquals(MediaType.IMAGE_PNG, response.headers.contentType)
        assertEquals("attachment; filename=\"$maskName\"", response.headers[HttpHeaders.CONTENT_DISPOSITION]?.first())
        assertEquals(resource, response.body)
    }

    @Test
    @WithMockUser(roles = ["USER", "ADMIN"])
    fun `test upload`() {
        val maskFile = MockMultipartFile("mask", "test.png", MediaType.IMAGE_PNG_VALUE, "test content".toByteArray())
        val maskDto = MaskDto(
            id = null,
            name = "name",
            path = "path",
            size = "size",
            format = "format",
            createdBy = null,
            createdById = null,
            likes = 0,
            tags = mutableListOf(),
            tagIds = mutableListOf(1L)
        )
        val createdMask = Mask("name", "path", "format")
        val createdMaskDto = MaskDto(
            id = 1L,
            name = "name",
            path = "path",
            size = "size",
            format = "format",
            createdBy = null,
            createdById = null,
            likes = 0,
            tags = mutableListOf(),
            tagIds = mutableListOf(1L)
        )

        `when`(maskService.create(maskFile, maskDto)).thenReturn(createdMask)
        `when`(maskMapper.toDto(createdMask)).thenReturn(createdMaskDto)

        val result = maskController.upload(maskFile, maskDto)

        assertEquals(createdMaskDto, result)
    }

    @Test
    @WithMockUser(roles = ["USER", "ADMIN"])
    fun `test delete`() {
        val response = maskController.delete(1L)

        verify(maskService, times(1)).delete(1L)
        assertEquals(HttpStatus.OK, response.statusCode)
    }

    @Test
    fun `test updateMask`() {
        val maskDto = MaskDto(
            id = 1L,
            name = "updated name",
            path = "updated path",
            size = "updated size",
            format = "updated format",
            createdBy = null,
            createdById = null,
            likes = 0,
            tags = mutableListOf(),
            tagIds = mutableListOf(1L)
        )
        val updatedMask = Mask("updated name", "updated path", "updated format")
        val updatedMaskDto = MaskDto(
            id = 1L,
            name = "updated name",
            path = "updated path",
            size = "updated size",
            format = "updated format",
            createdBy = null,
            createdById = null,
            likes = 0,
            tags = mutableListOf(),
            tagIds = mutableListOf(1L)
        )

        `when`(maskService.update(1L, maskDto)).thenReturn(updatedMask)
        `when`(maskMapper.toDto(updatedMask)).thenReturn(updatedMaskDto)

        val result = maskController.updateMask(1L, maskDto)

        assertEquals(updatedMaskDto, result)
    }

    @Test
    fun `test toggleMaskLike`() {
        val updatedMask = Mask("name", "path", "format")
        val updatedMaskDto = MaskDto(
            id = 1L,
            name = "name",
            path = "path",
            size = "size",
            format = "format",
            createdBy = null,
            createdById = null,
            likes = 1,
            tags = mutableListOf(),
            tagIds = mutableListOf(1L)
        )

        `when`(maskService.toggleLike(1L, 1L)).thenReturn(updatedMask)
        `when`(maskMapper.toDto(updatedMask)).thenReturn(updatedMaskDto)

        val result = maskController.toggleMaskLike(1L, 1L)

        assertEquals(updatedMaskDto, result)
    }
}

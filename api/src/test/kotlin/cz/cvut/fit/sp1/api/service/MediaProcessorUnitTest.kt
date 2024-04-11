package cz.cvut.fit.sp1.api.service

import cz.cvut.fit.sp1.api.component.FileStorage
import cz.cvut.fit.sp1.api.component.MediaProcessor
import cz.cvut.fit.sp1.api.exception.MediaException
import cz.cvut.fit.sp1.api.exception.exceptioncodes.MaskExceptionCodes
import cz.cvut.fit.sp1.api.exception.exceptioncodes.VideoExceptionCodes
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.function.Executable
import org.mockito.Mockito
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.web.multipart.MultipartFile
import java.io.InputStream
import kotlin.io.path.Path

@SpringBootTest
class MediaProcessorUnitTest {
    lateinit var mediaProcessor: MediaProcessor
    @MockBean
    lateinit var media: MultipartFile
    @MockBean
    lateinit var fileStorage: FileStorage
    val basePath = Path(System.getProperty("user.home"),"sp1", "storage").toString() // TODO need to create configuration with setters on path

    @BeforeEach
    fun setUp() {
        mediaProcessor = MediaProcessor(media, fileStorage)
    }

    @Test
    fun isVideoTrue() {
        Mockito.`when`(
            media.contentType
        ).thenReturn("video/mp4")
        Assertions.assertTrue(mediaProcessor.isVideo(media))
        Mockito.`when`(
            media.contentType
        ).thenReturn("video/avi")
        Assertions.assertTrue(mediaProcessor.isVideo(media))
    }
    @Test
    fun isVideoFalse() {
        Mockito.`when`(
            media.contentType
        ).thenReturn("image/png")
        Assertions.assertFalse(mediaProcessor.isVideo(media))
        Mockito.`when`(
            media.contentType
        ).thenReturn("image/jpeg")
        Assertions.assertFalse(mediaProcessor.isVideo(media))
    }
    @Test
    fun isMaksTrue() {
        Mockito.`when`(
            media.contentType
        ).thenReturn("image/png")
        Assertions.assertTrue(mediaProcessor.isMask(media))
    }
    @Test
    fun isMaksFalse() {
        Mockito.`when`(
            media.contentType
        ).thenReturn("image/svg")
        Assertions.assertFalse(mediaProcessor.isMask(media))
        Mockito.`when`(
            media.contentType
        ).thenReturn("image/jpeg")
        Assertions.assertFalse(mediaProcessor.isMask(media))
    }
    @Test
    fun extractVideoInfoSuccess() {
        Mockito.`when`(
            media.contentType
        ).thenReturn("video/mp4")
        Mockito.`when`(
            media.inputStream
        ).thenReturn(InputStream.nullInputStream())
        val video = mediaProcessor.extractVideoInfo()
        Assertions.assertEquals("mp4", video.format)
        Assertions.assertEquals(Path(basePath, "${video.name}.mp4").toString(), video.path)
//        Assertions.assertTrue( video.fps == 30L )
//        Assertions.assertTrue( video.duration >= 3 )

    }
    @Test
    fun extractVideoInfoFail() {
        Mockito.`when`(
            media.contentType
        ).thenReturn("image/png")
        val exception = Assertions.assertThrows(
            MediaException::class.java,
            Executable {
                mediaProcessor.extractVideoInfo()
            }
        )
        val expectedCode = VideoExceptionCodes.INVALID_VIDEO_FILE.code
        val actualCode = exception.code
        Assertions.assertTrue(expectedCode == actualCode)
    }
    @Test
    fun extractMaskInfoSuccess() {
        Mockito.`when`(
            media.contentType
        ).thenReturn("image/png")
        Mockito.`when`(
            media.size
        ).thenReturn(322L)
        Mockito.`when`(
            media.inputStream
        ).thenReturn(this::class.java.classLoader.getResourceAsStream("mask_test_0001.png"))
        val mask = mediaProcessor.extractMaskInfo()
        Assertions.assertEquals("png", mask.format)
        Assertions.assertEquals(Path(basePath, "${mask.name}.png").toString(), mask.path)
        Assertions.assertEquals(322L, mask.size)
        val width = 800;
        val height = 800;
        val aspectRatio = width.toDouble() / height.toDouble()
        Assertions.assertEquals(width, mask.width)
        Assertions.assertEquals(height, mask.height)
        Assertions.assertEquals(aspectRatio, mask.aspectRatio)
    }
    @Test
    fun extractMaskInfoFail() {
        Mockito.`when`(
            media.contentType
        ).thenReturn("image/svg")
        val exception = Assertions.assertThrows(
            MediaException::class.java,
            Executable {
                mediaProcessor.extractMaskInfo()
            }
        )
        val expectedCode = MaskExceptionCodes.INVALID_MASK_FILE.code
        val actualCode = exception.code
        Assertions.assertTrue(expectedCode == actualCode)
    }
}
package cz.cvut.fit.sp1.api.component

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import cz.cvut.fit.sp1.api.configuration.StoragePathProperties
import cz.cvut.fit.sp1.api.data.dto.VideoInfoResponse
import cz.cvut.fit.sp1.api.data.model.media.Mask
import cz.cvut.fit.sp1.api.data.model.media.Video
import cz.cvut.fit.sp1.api.exception.MediaException
import cz.cvut.fit.sp1.api.exception.ValidationException
import cz.cvut.fit.sp1.api.exception.exceptioncodes.MaskExceptionCodes
import cz.cvut.fit.sp1.api.exception.exceptioncodes.VideoExceptionCodes
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpMethod
import org.springframework.http.MediaType
import org.springframework.util.LinkedMultiValueMap
import org.springframework.web.client.RestTemplate
import org.springframework.web.multipart.MultipartFile
import java.text.SimpleDateFormat
import java.util.*
import javax.imageio.ImageIO
import kotlin.io.path.Path

class MediaProcessor(
    private val media: MultipartFile,
    private val fileStorage: FileStorage,
    private val restTemplate: RestTemplate,
    private val storagePathProperties: StoragePathProperties,
) {
    companion object {
        val objectMapper = ObjectMapper()
    }

    fun extractVideoInfo(): Video {
        // need to check if it's video before conversion to one format
        if (!isVideo(media)) throw MediaException(VideoExceptionCodes.INVALID_VIDEO_FILE)

        val name = generateFileName("video")
        val extension = determineFileType(media)
        val filePath = Path(storagePathProperties.mediaPath, "$name.$extension").toString()

        saveFile(filePath)

        val videoInfo = getVideoInfo(filePath)

        val videoEntity =
            Video(
                name = name,
                path = videoInfo.output_file,
                format = ".mp4",
            )
        videoEntity.fps = videoInfo.fps.toDouble()
        videoEntity.height = videoInfo.height
        videoEntity.width = videoInfo.width
        videoEntity.previewPath = videoInfo.preview
        videoEntity.duration = videoInfo.duration
        videoEntity.previewPath = videoInfo.preview
        videoEntity.size = videoInfo.size
        return videoEntity
    }

    private fun getVideoInfo(path: String): VideoInfoResponse {
        val headers = HttpHeaders()
        headers.accept = listOf(MediaType.APPLICATION_JSON)

        val request = HttpEntity(LinkedMultiValueMap<String, String>(), headers)
        val response =
            restTemplate.exchange(
                "http://127.0.0.1:5000/prepare?path=$path",
                HttpMethod.GET,
                request,
                String::class.java,
            )
        val connectToSlackResponseString =
            response.body ?: let {
                val exception = ValidationException(VideoExceptionCodes.PYTHON_INFO_ERROR)
                throw exception
            }

        val parsed = objectMapper.readValue<VideoInfoResponse>(connectToSlackResponseString)
        if (!parsed.success) {
            throw ValidationException(VideoExceptionCodes.PYTHON_INFO_ERROR)
        }

        return parsed
    }

    private fun buildMask(
        name: String,
        extension: String,
        filePath: String,
    ): Mask {
        val mask =
            Mask(
                name = name,
                path = filePath,
                format = extension,
            )
        val inputStream = media.inputStream
        val image = ImageIO.read(inputStream)

        mask.size = media.size.toString()
        mask.width = image.width
        mask.height = image.height
        mask.aspectRatio = mask.width.toDouble() / mask.height.toDouble()

        return mask
    }

    fun extractMaskInfo(): Mask {
        // need to check if it's mask (png format image) before saving
        if (!isMask(media)) throw MediaException(MaskExceptionCodes.INVALID_MASK_FILE)

        val name = generateFileName("mask")
        val extension = determineFileType(media)
        val filePath = Path(storagePathProperties.mediaPath, "$name.$extension").toString()

        val mask = buildMask(name, extension, filePath)

        saveFile(filePath)

        return mask
    }

    private fun generateFileName(type: String): String {
        val dateFormat = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault())
        val date = dateFormat.format(Date())
        val random = (1000..9999).random()
        return "${type}_${date}_$random"
    }

    private fun determineFileType(mediaSource: MultipartFile): String {
        return mediaSource.contentType?.split("/")?.last() ?: ""
    }

    fun isVideo(videoSource: MultipartFile): Boolean {
        return videoSource.contentType?.split("/")?.first() == "video"
    }

    fun isMask(maskSource: MultipartFile): Boolean {
        return maskSource.contentType?.split("/")?.last() == "png"
    }

    private fun saveFile(fileName: String) {
        val inputStream = media.inputStream
        val bytes = inputStream.readBytes()
        inputStream.close()

        fileStorage.store(fileName, bytes = bytes)
    }
}

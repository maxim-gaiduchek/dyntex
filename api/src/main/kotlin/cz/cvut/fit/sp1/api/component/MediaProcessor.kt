package cz.cvut.fit.sp1.api.component

import cz.cvut.fit.sp1.api.data.model.media.Mask
import cz.cvut.fit.sp1.api.data.model.media.Video
import cz.cvut.fit.sp1.api.exception.MediaFileIsNotMaskException
import cz.cvut.fit.sp1.api.exception.MediaFileIsNotVideoException
import cz.cvut.fit.sp1.api.exception.exceptioncodes.ValidationExceptionCodes
import org.springframework.web.multipart.MultipartFile
import java.text.SimpleDateFormat
import java.util.*
import javax.imageio.ImageIO
import kotlin.io.path.Path

class MediaProcessor(
    private val media: MultipartFile,
    private val fileStorage: FileStorage,
) {
    companion object {
        val basePath = Path(System.getProperty("user.home"),"sp1", "storage").toString() // TODO need to create configuration with setters on path
    }

    fun extractVideoInfo(): Video {
        // need to check if it's video before conversion to one format
        if (!isVideo(media)) throw MediaFileIsNotVideoException(ValidationExceptionCodes.INVALID_VIDEO_FILE)

        val name = generateFileName("video")
        val extension = determineFileType(media)
        val filePath = Path(basePath, "$name.$extension").toString()

        saveFile(filePath)

        // nado poprosit vanu ctoby on na pythone napisal script kotory by dostal ese info iz file

        return Video(
            name = name,
            path = filePath,
            format = extension,
        )
    }

    fun extractMaskInfo() : Mask {
        // need to check if it's mask (png format image) before saving
        if (!isMask(media)) throw MediaFileIsNotMaskException(ValidationExceptionCodes.INVALID_MASK_FILE)

        val name = generateFileName("mask")
        val extension = determineFileType(media)
        val filePath = Path(basePath, "$name.$extension").toString()

        val mask = Mask(
            name = name,
            path = filePath,
            format = extension,
        )
        val inputStream = media.inputStream
        val image = ImageIO.read(inputStream)

        mask.size = media.size
        mask.width = image.width
        mask.height = image.height
        mask.aspectRatio = mask.width.toDouble() / mask.height.toDouble()

        saveFile(filePath)
        return mask
    }

    private fun generateFileName(type : String): String {
        val dateFormat = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault())
        val date = dateFormat.format(Date())
        val random = (1000..9999).random()
        return "${type}_${date}_${random}"
    }

    private fun determineFileType(videoSource: MultipartFile): String {
        return videoSource.contentType?.split("/")?.last() ?: ""
    }

    fun isVideo(videoSource: MultipartFile) : Boolean {
        return videoSource.contentType?.split("/")?.first() == "video"
    }

    fun isMask(maskSource: MultipartFile) : Boolean {
        return maskSource.contentType?.split("/")?.last() == "png"
    }

    private fun saveFile(path: String) {
        val inputStream = media.inputStream
        val bytes = inputStream.readBytes()
        inputStream.close()

        fileStorage.store(path, bytes = bytes)
    }
}

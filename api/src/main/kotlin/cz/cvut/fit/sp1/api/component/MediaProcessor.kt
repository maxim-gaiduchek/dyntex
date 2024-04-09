package cz.cvut.fit.sp1.api.component

import cz.cvut.fit.sp1.api.data.model.media.Video
import org.springframework.web.multipart.MultipartFile
import java.text.SimpleDateFormat
import java.util.*

class MediaProcessor(
    private val media: MultipartFile,
    private val fileStorage: FileStorage,
) {
    companion object {
        val basePath = "${System.getProperty("user.home")}/sp1/storage" // TODO need to create configuration with setters on path
    }

    fun extractVideoInfo(): Video {
        val name = generateFileName()
        val extension = determineFileType(media)
        val filePath = "$basePath/$name.$extension"

        saveFile(filePath)

        // nado poprosit vanu ctoby on na pythone napisal script kotory by dostal ese info iz file

        return Video(
            name = name,
            path = filePath,
            format = extension,
        )
    }

    private fun generateFileName(): String {
        val dateFormat = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault())
        val date = dateFormat.format(Date())
        val random = (1000..9999).random()
        return "video_${date}_$random"
    }

    private fun determineFileType(videoSource: MultipartFile): String {
        return videoSource.contentType?.split("/")?.last() ?: ""
    }

    private fun saveFile(path: String) {
        val inputStream = media.inputStream
        val bytes = inputStream.readBytes()
        inputStream.close()

        fileStorage.store(path, bytes = bytes)
    }
}

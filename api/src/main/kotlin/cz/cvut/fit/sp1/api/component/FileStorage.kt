package cz.cvut.fit.sp1.api.component

import cz.cvut.fit.sp1.api.exception.FileStoreException
import cz.cvut.fit.sp1.api.exception.exceptioncodes.FileStoreExceptionCodes
import org.springframework.core.io.InputStreamResource
import org.springframework.core.io.Resource
import org.springframework.core.io.UrlResource
import org.springframework.core.io.support.ResourceRegion
import org.springframework.stereotype.Component
import java.io.File
import java.io.FileInputStream
import kotlin.math.min

@Component
class FileStorage {
    fun store(
        path: String,
        bytes: ByteArray,
    ) {
        val file = getFile(path)

        try {
            file.writeBytes(bytes)
        } catch (e: Exception) {
            throw FileStoreException(FileStoreExceptionCodes.STORE_FILE_ERROR)
        }
    }

    fun delete(path: String) {
        val file = getFile(path)
        if (file.exists()) {
            file.delete()
        }
    }

    fun readFileAsBytes(storageFilePath: String): ByteArray {
        try {
            return getFile(storageFilePath).readBytes()
        } catch (e: Exception) {
            throw FileStoreException(FileStoreExceptionCodes.READ_FILE_ERROR, storageFilePath)
        }
    }

    fun getFile(path: String): File {
        val file = File(path)
        /*if (!file.) {
            //throw FileStoreException(FileStoreExceptionCodes.READ_FILE_ERROR, path)
        }*/
        return file
    }

    private fun getFileUrlResource(filePath: String): UrlResource {
        val file = getFile(filePath)
        return UrlResource(file.toURI())
    }

    fun parseRange(
        rangeHeader: String,
        contentLength: Long,
    ): Pair<Long, Long> {
        val range = rangeHeader.replace("bytes=", "").split("-")
        val start = range[0].toLong()
        val end = if (range.size > 1 && range[1].isNotEmpty()) range[1].toLong() else contentLength - 1
        return start to end
    }

    fun getResourceRegion(
        videoPath: String,
        rangeHeader: String?,
    ): ResourceRegion {
        val resource = getFileUrlResource(videoPath)
        val contentLength = resource.contentLength()

        return if (rangeHeader != null) {
            val (start, end) = parseRange(rangeHeader, contentLength)
            val rangeLength = min(1 * 1024 * 1024, end - start + 1)
            ResourceRegion(resource, start, rangeLength)
        } else {
            ResourceRegion(resource, 0, min(1 * 1024 * 1024, contentLength))
        }
    }

    fun readFileAsResource(filePath: String): Resource {
        val file = getFile(filePath)
        return InputStreamResource(FileInputStream(file))
    }
}

package cz.cvut.fit.sp1.api.component

import org.springframework.stereotype.Component
import java.io.File

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
            // throw FileStorageException(FileStorageExceptionCodes.FILE_STORE_ERROR)
        }
    }

    fun delete(path: String) {
        val file = getFile(path)
        if (file.exists()) {
            file.delete()
        }
    }

    fun getFile(path: String) = File(path)
}

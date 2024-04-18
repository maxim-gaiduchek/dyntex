package cz.cvut.fit.sp1.api.component

import cz.cvut.fit.sp1.api.exception.FileStoreException
import cz.cvut.fit.sp1.api.exception.exceptioncodes.FileStoreExceptionCodes
import org.springframework.stereotype.Component
import java.io.File

@Component
class FileStorage() {
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

    fun readFile(storageFilePath: String): ByteArray {
        try {
            return getFile(storageFilePath).readBytes()
        } catch (e: Exception) {
            throw FileStoreException(FileStoreExceptionCodes.READ_FILE_ERROR, storageFilePath)
        }
    }

    fun getFile(path: String) = File(path)
}

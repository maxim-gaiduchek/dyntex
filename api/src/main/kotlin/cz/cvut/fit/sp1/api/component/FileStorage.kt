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
            println("MAMA ${e.message}")
        }
    }

    fun getFile(path: String) = File(path)
}

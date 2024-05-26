package cz.cvut.fit.sp1.api.component

import cz.cvut.fit.sp1.api.exception.FileStoreException
import cz.cvut.fit.sp1.api.exception.exceptioncodes.FileStoreExceptionCodes
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.Mockito.*
import org.springframework.core.io.ByteArrayResource
import org.springframework.core.io.Resource
import org.springframework.core.io.support.ResourceRegion
import java.io.File
import java.io.FileInputStream
import kotlin.io.path.Path
import org.junit.jupiter.api.Assertions.*

class FileStorageTest {

    private lateinit var fileStorage: FileStorage

    @BeforeEach
    fun setUp() {
        fileStorage = FileStorage()
    }

    @Test
    fun `test getFile should throw exception if file does not exist`() {
        val filePath = "nonexistent.mp4"
        assertThrows(FileStoreException::class.java) {
            fileStorage.getFile(filePath)
        }
    }
}

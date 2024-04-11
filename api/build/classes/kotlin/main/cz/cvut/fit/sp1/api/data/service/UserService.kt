package cz.cvut.fit.sp1.api.data.service

import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile
import cz.cvut.fit.sp1.api.data.repository.MaskRepository
import cz.cvut.fit.sp1.api.component.FileStorage
import cz.cvut.fit.sp1.api.data.model.media.Mask

@Service
class UserService(
    private val maskRepository: MaskRepository,
    private val fileStorage: FileStorage
) {

    fun create(mask: MultipartFile): Mask {
        // Logic to handle file upload and mask creation
    }

    fun get(id: Long): Mask {
        // Logic to retrieve a mask by id
    }

    fun delete(id: Long): Boolean {
        // Logic to delete a mask by id
        // Return true if successful, false otherwise
    }
}

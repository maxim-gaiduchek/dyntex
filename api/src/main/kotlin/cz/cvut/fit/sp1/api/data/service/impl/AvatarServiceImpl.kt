package cz.cvut.fit.sp1.api.data.service.impl

import cz.cvut.fit.sp1.api.component.FileStorage
import cz.cvut.fit.sp1.api.data.model.media.Avatar
import cz.cvut.fit.sp1.api.data.repository.AvatarRepository
import cz.cvut.fit.sp1.api.data.service.interfaces.AvatarService
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile

@Service
class AvatarServiceImpl(
    private val avatarRepository: AvatarRepository,
    private val fileStorage: FileStorage
) : AvatarService {

    override fun save(userId: Long, multipartFile: MultipartFile): Avatar {
        val fileName = "user_avatar_${userId}_${System.currentTimeMillis()}_${multipartFile.originalFilename}"
        fileStorage.store(fileName, multipartFile.bytes)
        return Avatar(
            name = fileName,
            path = fileName,
            height = 0,
            width = 0
        )
    }

    override fun delete(id: Long): Avatar {
        val avatar = avatarRepository.findById(id)
            .orElseThrow { jakarta.persistence.EntityNotFoundException("Avatar not found with id: $id") }

        fileStorage.delete(avatar.path)

        avatarRepository.delete(avatar)

        return avatar
    }
}




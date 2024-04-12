package cz.cvut.fit.sp1.api.data.service.interfaces

import cz.cvut.fit.sp1.api.data.model.media.Avatar
import org.springframework.web.multipart.MultipartFile

interface AvatarService {

    fun save(multipartFile: MultipartFile): Avatar

    fun delete(id: Long): Avatar
}

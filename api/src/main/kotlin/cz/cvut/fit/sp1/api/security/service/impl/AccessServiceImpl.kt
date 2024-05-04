package cz.cvut.fit.sp1.api.security.service.impl

import cz.cvut.fit.sp1.api.data.model.AccountRole
import cz.cvut.fit.sp1.api.data.service.interfaces.VideoService
import cz.cvut.fit.sp1.api.security.model.TokenAuthentication
import cz.cvut.fit.sp1.api.security.service.interfaces.AccessService
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Service

@Service("accessService")
class AccessServiceImpl(
    private val videoService: VideoService
) : AccessService {

    override fun hasUserAccessToUpdateUser(userId: Long?): Boolean {
        userId ?: return false
        val auth = fetchAuthentication() ?: return false
        return auth.userId == userId
    }

    override fun hasUserAccessToUpdateVideo(videoId: Long?): Boolean {
        videoId ?: return false
        val auth = fetchAuthentication() ?: return false
        if (auth.role == AccountRole.ADMIN) {
            return true
        }
        val video = videoService.getByIdOrThrow(videoId)
        video.createdBy ?: return false
        return auth.userId == video.createdBy!!.id
    }

    private fun fetchAuthentication(): TokenAuthentication? {
        return SecurityContextHolder.getContext().authentication as? TokenAuthentication
    }
}
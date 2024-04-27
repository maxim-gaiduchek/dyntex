package cz.cvut.fit.sp1.api.security.service.impl

import cz.cvut.fit.sp1.api.data.model.AccountRole
import cz.cvut.fit.sp1.api.data.service.impl.MaskServiceImpl
import cz.cvut.fit.sp1.api.data.service.interfaces.VideoService
import cz.cvut.fit.sp1.api.security.model.TokenAuthentication
import cz.cvut.fit.sp1.api.security.service.interfaces.AccessService
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Service

@Service("accessService")
class AccessServiceImpl(
    private val videoService: VideoService,
    private val maskService: MaskServiceImpl
) : AccessService {

    override fun hasUserAccessToUpdateUser(userId: Long?): Boolean {
        userId ?: return false
        val auth = fetchAuthentication() ?: return false
        return auth.userId == userId
    }

    override fun hasUserAccessToUpdateMask(maskId: Long?): Boolean {
        maskId ?: return false
        val auth = fetchAuthentication() ?: return false
        if (auth.role == AccountRole.ADMIN) {
            return true
        }
        val mask = maskService.getByIdOrThrow(maskId)
        return auth.userId == mask.createdBy?.id
    }

    override fun hasUserAccessToUpdateVideo(videoId: Long?): Boolean {
        videoId ?: return false
        val auth = fetchAuthentication() ?: return false
        if (auth.role == AccountRole.ADMIN) {
            return true
        }
        val video = videoService.getByIdOrThrow(videoId)
        return auth.userId == video.createdBy?.id
    }

    private fun fetchAuthentication(): TokenAuthentication? {
        return SecurityContextHolder.getContext().authentication as? TokenAuthentication
    }
}
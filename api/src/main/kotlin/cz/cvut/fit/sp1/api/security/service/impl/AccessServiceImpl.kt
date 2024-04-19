package cz.cvut.fit.sp1.api.security.service.impl

import cz.cvut.fit.sp1.api.security.model.TokenAuthentication
import cz.cvut.fit.sp1.api.security.service.interfaces.AccessService
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Service

@Service("accessService")
class AccessServiceImpl : AccessService {

    override fun hasUserAccessToUpdateUser(userId: Long?): Boolean {
        userId ?: return false
        val auth = fetchAuthentication() ?: return false
        return auth.userId == userId
    }

    private fun fetchAuthentication(): TokenAuthentication? {
        return SecurityContextHolder.getContext().authentication as? TokenAuthentication
    }
}
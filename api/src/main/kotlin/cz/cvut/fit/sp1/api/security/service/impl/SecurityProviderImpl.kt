package cz.cvut.fit.sp1.api.security.service.impl

import cz.cvut.fit.sp1.api.security.model.TokenAuthentication
import cz.cvut.fit.sp1.api.security.service.interfaces.SecurityProvider
import org.springframework.context.annotation.Profile
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Service

@Service
@Profile("!local")
class SecurityProviderImpl : SecurityProvider {

    override fun fetchAuthenticatedUserId(): Long? {
        val auth = SecurityContextHolder.getContext().authentication as? TokenAuthentication
        auth ?: return null
        return auth.userId
    }
}
package cz.cvut.fit.sp1.api.security.service.impl

import cz.cvut.fit.sp1.api.security.service.interfaces.SecurityProvider
import org.springframework.context.annotation.Profile
import org.springframework.stereotype.Service

@Service
@Profile("local")
class LocalSecurityProviderImpl : SecurityProvider {

    override fun fetchAuthenticatedUserId(): Long? {
        return 1L
    }
}
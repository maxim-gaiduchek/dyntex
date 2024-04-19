package cz.cvut.fit.sp1.api.security.service.interfaces

interface SecurityProvider {

    fun fetchAuthenticatedUserId(): Long?
}
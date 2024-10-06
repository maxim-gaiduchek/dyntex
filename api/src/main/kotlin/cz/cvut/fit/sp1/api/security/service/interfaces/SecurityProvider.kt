package cz.cvut.fit.sp1.api.security.service.interfaces

fun interface SecurityProvider {

    fun fetchAuthenticatedUserId(): Long?
}
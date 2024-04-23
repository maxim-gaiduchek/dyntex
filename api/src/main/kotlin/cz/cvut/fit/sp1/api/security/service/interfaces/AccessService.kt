package cz.cvut.fit.sp1.api.security.service.interfaces

interface AccessService {

    fun hasUserAccessToUpdateUser(userId: Long?): Boolean
    fun hasUserAccessToUpdateVideo(videoId: Long?): Boolean
}
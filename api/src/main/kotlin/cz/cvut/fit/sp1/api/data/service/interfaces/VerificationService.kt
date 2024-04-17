package cz.cvut.fit.sp1.api.data.service.interfaces

interface VerificationService {
    fun sendVerificationEmail(email: String, token: String)

    fun verifyToken(authToken: String?)

}
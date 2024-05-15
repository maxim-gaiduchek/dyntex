package cz.cvut.fit.sp1.api.data.service.interfaces

interface AuthService {

    fun sendVerificationEmail(email: String, token: String)

    fun verifyToken(authToken: String?)

    fun sendRecoveryEmail(email: String, token: String)
}
package cz.cvut.fit.sp1.api.data.service.impl

import cz.cvut.fit.sp1.api.data.service.interfaces.VerificationService
import cz.cvut.fit.sp1.api.exception.ValidationException
import cz.cvut.fit.sp1.api.exception.exceptioncodes.UserAccountExceptionCodes
import org.springframework.stereotype.Service
import java.util.*

@Service
class VerificationServiceImpl(
        private val emailService: EmailServiceImpl,
        private val userAccountServiceImpl: UserAccountServiceImpl
) : VerificationService {

    companion object {
        private const val MINUTES_TO_EXPIRE = 5
    }

    override fun sendVerificationEmail(email: String, token: String) {
        val confirmationUrl = "http://localhost:8080/api/auth/verify?authToken=$token"
        emailService.sendEmail(email, "Verify your account", "Follow the link to confirm: $confirmationUrl")
    }

    override fun verifyToken(authToken: String?) {
        checkVerifyPossibility(authToken)
        val user = userAccountServiceImpl.findByAuthToken(authToken!!)
        if (Date().time <= user.createdAt!!.time + (MINUTES_TO_EXPIRE * 60 * 1000)) {
            user.authEnable = true
            return
        }
        userAccountServiceImpl.delete(user.id)
    }

    private fun checkVerifyPossibility(authToken: String?) {
        if(authToken.isNullOrBlank()){
            throw ValidationException( UserAccountExceptionCodes.AUTH_TOKEN_IS_EMPTY )
        }
    }
}
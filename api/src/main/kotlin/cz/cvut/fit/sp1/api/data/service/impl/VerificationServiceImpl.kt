package cz.cvut.fit.sp1.api.data.service.impl

import cz.cvut.fit.sp1.api.data.service.interfaces.EmailService
import cz.cvut.fit.sp1.api.data.service.interfaces.UserAccountService
import cz.cvut.fit.sp1.api.data.service.interfaces.VerificationService
import cz.cvut.fit.sp1.api.exception.ValidationException
import cz.cvut.fit.sp1.api.exception.exceptioncodes.UserAccountExceptionCodes
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import java.util.*

@Service
class VerificationServiceImpl(
    private val emailService: EmailService,
    private val userAccountService: UserAccountService,
    @Value("\${mail.domain-url}") private val mailDomain : String,
) : VerificationService {

    companion object {
        private const val MINUTES_TO_EXPIRE = 60
    }

    override fun sendVerificationEmail(email: String, token: String ,) {

        val confirmationUrl = "http://$mailDomain/api/auth/verify?authToken=$token"
        val emailContent = """
        <html>
            <body>
                <p>Follow the link to confirm your account:</p>
                <a href="$confirmationUrl">Verify Account</a>
            </body>
        </html>
    """.trimIndent()
        emailService.sendEmail(email, "Verify your account", emailContent)
    }

    override fun verifyToken(authToken: String?) {
        checkVerifyPossibility(authToken)
        val user = userAccountService.findByAuthToken(authToken!!)
        if (Date().time <= user.createdAt!!.time + (MINUTES_TO_EXPIRE * 60 * 1000)) {
            user.authEnable = true
            userAccountService.save(user)
            return
        }
        userAccountService.delete(user.id)
    }

    private fun checkVerifyPossibility(authToken: String?) {
        if (authToken.isNullOrBlank()) {
            throw ValidationException(UserAccountExceptionCodes.AUTH_TOKEN_IS_EMPTY)
        }
    }
}
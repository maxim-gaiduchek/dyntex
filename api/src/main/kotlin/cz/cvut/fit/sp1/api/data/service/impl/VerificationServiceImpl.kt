package cz.cvut.fit.sp1.api.data.service.impl

import cz.cvut.fit.sp1.api.data.model.UserAccount
import cz.cvut.fit.sp1.api.data.service.interfaces.EmailService
import cz.cvut.fit.sp1.api.data.service.interfaces.UserAccountService
import cz.cvut.fit.sp1.api.data.service.interfaces.VerificationService
import cz.cvut.fit.sp1.api.exception.ValidationException
import cz.cvut.fit.sp1.api.exception.exceptioncodes.UserAccountExceptionCodes
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import org.thymeleaf.TemplateEngine
import org.thymeleaf.context.Context
import java.util.*

@Service
class VerificationServiceImpl(
    private val emailService: EmailService,
    private val userAccountService: UserAccountService,
    @Value("\${verification.mail.url}") private val mailUrl: String,
    @Value("\${verification.token-expiring-time}") private val tokenExpire: Int,
    private val templateEngine: TemplateEngine
) : VerificationService {


    override fun sendVerificationEmail(email: String, token: String) {
        val confirmationUrl = "$mailUrl/verify?authToken=$token"
        val context = Context().apply {
            setVariable("confirmationUrl", confirmationUrl)
        }
        val emailContent = templateEngine.process("verification_email", context)
        emailService.sendEmail(email, "Verify your account", emailContent)
    }

    override fun verifyToken(authToken: String?) {
        checkVerifyPossibility(authToken)
        val user = userAccountService.getByAuthToken(authToken!!)
        if (expireCheck(user)) {
            user.authEnable = true
            userAccountService.save(user)
            return
        }
        userAccountService.delete(user.id)
        throw ValidationException(UserAccountExceptionCodes.AUTH_TOKEN_IS_EXPIRED)
    }

    private fun expireCheck(user: UserAccount): Boolean {
        return Date().time <= user.createdAt!!.time + (tokenExpire * 60 * 1000)
    }

    private fun checkVerifyPossibility(authToken: String?) {
        if (authToken.isNullOrBlank()) {
            throw ValidationException(UserAccountExceptionCodes.AUTH_TOKEN_INVALID)
        }
    }
}
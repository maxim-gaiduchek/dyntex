package cz.cvut.fit.sp1.api.data.service.impl

import cz.cvut.fit.sp1.api.data.model.UserAccount
import cz.cvut.fit.sp1.api.data.service.interfaces.AuthService
import cz.cvut.fit.sp1.api.data.service.interfaces.EmailService
import cz.cvut.fit.sp1.api.data.service.interfaces.UserAccountService
import cz.cvut.fit.sp1.api.exception.AccessDeniedException
import cz.cvut.fit.sp1.api.exception.ValidationException
import cz.cvut.fit.sp1.api.exception.exceptioncodes.AccessExceptionCodes
import cz.cvut.fit.sp1.api.exception.exceptioncodes.UserAccountExceptionCodes
import cz.cvut.fit.sp1.api.security.constants.JwtClaimsConstants
import cz.cvut.fit.sp1.api.security.data.dto.JwtRequest
import cz.cvut.fit.sp1.api.security.data.dto.JwtResponse
import cz.cvut.fit.sp1.api.security.service.interfaces.JwtProvider
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import org.thymeleaf.TemplateEngine
import org.thymeleaf.context.Context
import java.util.*

@Service
class AuthServiceImpl(
    private val emailService: EmailService,
    private val userAccountService: UserAccountService,
    @Value("\${verification.mail.url}") private val mailUrl: String,
    @Value("\${verification.token-expiring-time}") private val tokenExpire: Int,
    private val templateEngine: TemplateEngine,
    private val jwtProvider: JwtProvider,
) : AuthService {


    override fun sendVerificationEmail(email: String, token: String) {
        val confirmationUrl = "$mailUrl/verify?authToken=$token"
        val emailContent = getEmailContent(confirmationUrl, "verification_email")
        emailService.sendEmail(email, "Verify your account", emailContent)
    }

    override fun sendRecoveryEmail(email: String, token: String) {
        val confirmationUrl = "$mailUrl/recovery?t=$token"
        val emailContent = getEmailContent(confirmationUrl, "received_email")
        emailService.sendEmail(email, "Password recovery", emailContent)
    }

    private fun getEmailContent(confirmationUrl: String, emailTemplate: String): String {
        val context = Context().apply {
            setVariable("confirmationUrl", confirmationUrl)
        }
        return templateEngine.process(emailTemplate, context)
    }

    override fun verifyToken(authToken: String?) {
        checkVerifyPossibility(authToken)
        val user = userAccountService.getByAuthToken(authToken!!)
        if (!expireCheck(user)) {
            userAccountService.delete(user.id)
            throw ValidationException(UserAccountExceptionCodes.AUTH_TOKEN_IS_EXPIRED)
        }
        user.authEnable = true
        userAccountService.save(user)
    }

    private fun expireCheck(user: UserAccount): Boolean {
        return Date().time <= user.createdAt!!.time + (tokenExpire * 60 * 1000)
    }

    private fun checkVerifyPossibility(authToken: String?) {
        if (authToken.isNullOrBlank()) {
            throw ValidationException(UserAccountExceptionCodes.AUTH_TOKEN_INVALID)
        }
    }

    override fun login(authRequest: JwtRequest): JwtResponse {
        val user = userAccountService.getByEmailOrThrow(authRequest.login!!)
        val requestPassword = authRequest.password?.lowercase()
        if (user.password != requestPassword) {
            throw AccessDeniedException(AccessExceptionCodes.INVALID_PASSWORD)
        }
        val accessToken = jwtProvider.generateAccessToken(user)
        val refreshToken = jwtProvider.generateRefreshToken(user)
        user.refreshTokens.add(refreshToken)
        userAccountService.save(user)
        return JwtResponse(
            accessToken = accessToken,
            refreshToken = refreshToken,
            userId = user.id,
            role = user.role,
        )
    }

    override fun logout(refreshToken: String) {
        val user = userAccountService.getByRefreshTokenOrThrow(refreshToken)
        user.refreshTokens.remove(refreshToken)
        userAccountService.save(user)
    }

    override fun getAccessToken(refreshToken: String?): JwtResponse {
        if (jwtProvider.isAccessTokenValid(refreshToken)) {
            return JwtResponse()
        }
        val claims = jwtProvider.getRefreshClaims(refreshToken!!)
        val userId = claims.get(JwtClaimsConstants.USER_ID_KEY, Long::class.java)
        val user = userAccountService.getByIdOrThrow(userId)
        val refreshTokens = user.refreshTokens
        if (!refreshTokens.contains(refreshToken)) {
            return JwtResponse()
        }
        val accessToken = jwtProvider.generateAccessToken(user)
        return JwtResponse(
            accessToken = accessToken,
            userId = user.id,
            role = user.role,
        )
    }

    override fun refresh(refreshToken: String?): JwtResponse {
        if (!jwtProvider.isRefreshTokenValid(refreshToken)) {
            throw AccessDeniedException(AccessExceptionCodes.INVALID_JWT)
        }
        val claims = jwtProvider.getRefreshClaims(refreshToken!!)
        val userId = claims.get(JwtClaimsConstants.USER_ID_KEY, Long::class.java)
        val user = userAccountService.getByIdOrThrow(userId)
        val refreshTokens = user.refreshTokens
        if (!refreshTokens.contains(refreshToken)) {
            throw AccessDeniedException(AccessExceptionCodes.INVALID_JWT)
        }
        val accessToken = jwtProvider.generateAccessToken(user)
        val newRefreshToken = jwtProvider.generateRefreshToken(user)
        refreshTokens.add(newRefreshToken)
        userAccountService.save(user)
        return JwtResponse(
            accessToken = accessToken,
            refreshToken = refreshToken,
            userId = user.id,
            role = user.role,
        )
    }
}
package cz.cvut.fit.sp1.api.security.controller

import cz.cvut.fit.sp1.api.data.service.interfaces.AuthService
import cz.cvut.fit.sp1.api.exception.AccessDeniedException
import cz.cvut.fit.sp1.api.exception.exceptioncodes.AccessExceptionCodes
import cz.cvut.fit.sp1.api.security.data.dto.JwtRequest
import cz.cvut.fit.sp1.api.security.data.dto.JwtResponse
import cz.cvut.fit.sp1.api.utils.CookieUtils
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import jakarta.validation.Valid
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.HttpHeaders
import org.springframework.http.ResponseCookie
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController("/security")
class AuthController(
    private val authService: AuthService,
    @Value("\${jwt.cookie.age.refresh}") val refreshTokenAge: Long,
) {

    companion object {
        private const val REFRESH_TOKEN_COOKIE_NAME: String = "refreshToken"
    }

    @PostMapping("/login")
    fun login(
        @Valid @RequestBody authRequest: JwtRequest,
        httpResponse: HttpServletResponse
    ): JwtResponse {
        val jwtResponse = authService.login(authRequest)
        val newRefreshTokenCookie = createRefreshTokenCookie(jwtResponse.refreshToken)
        httpResponse.addHeader(HttpHeaders.SET_COOKIE, newRefreshTokenCookie.toString())
        return jwtResponse
    }

    private fun createRefreshTokenCookie(refreshToken: String?): ResponseCookie? {
        refreshToken ?: return null
        return CookieUtils.createHttpOnlyCookie(
            REFRESH_TOKEN_COOKIE_NAME, refreshToken, "/", refreshTokenAge
        )
    }

    @GetMapping("/logout")
    fun logout(httpRequest: HttpServletRequest, httpResponse: HttpServletResponse) {
        val refreshTokenCookie = CookieUtils.getCookie(httpRequest.cookies, REFRESH_TOKEN_COOKIE_NAME)
            ?: throw AccessDeniedException(AccessExceptionCodes.EXPIRED_JWT)
        authService.logout(refreshTokenCookie.value)
        CookieUtils.deleteCookie(httpRequest.cookies, httpResponse, REFRESH_TOKEN_COOKIE_NAME)
    }

    @GetMapping("/access")
    fun getNewAccessToken(httpRequest: HttpServletRequest): JwtResponse {
        val refreshTokenCookie = CookieUtils.getCookie(httpRequest.cookies, REFRESH_TOKEN_COOKIE_NAME)
        val refreshToken = refreshTokenCookie?.value
        return authService.getAccessToken(refreshToken)
    }

    @GetMapping("/refresh")
    fun refreshAccessToken(httpRequest: HttpServletRequest, httpResponse: HttpServletResponse): JwtResponse {
        val refreshTokenCookie = CookieUtils.getCookie(httpRequest.cookies, REFRESH_TOKEN_COOKIE_NAME)
        val refreshToken = refreshTokenCookie?.value
        val jwtResponse = authService.refresh(refreshToken)
        val newRefreshTokenCookie = createRefreshTokenCookie(jwtResponse.refreshToken)
        httpResponse.addHeader(HttpHeaders.SET_COOKIE, newRefreshTokenCookie.toString())
        return jwtResponse
    }
}
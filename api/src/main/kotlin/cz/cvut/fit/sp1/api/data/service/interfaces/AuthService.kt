package cz.cvut.fit.sp1.api.data.service.interfaces

import cz.cvut.fit.sp1.api.security.data.dto.JwtRequest
import cz.cvut.fit.sp1.api.security.data.dto.JwtResponse

interface AuthService {

    fun sendVerificationEmail(email: String, token: String)

    fun verifyToken(authToken: String?)

    fun sendRecoveryEmail(email: String, token: String)

    fun login(authRequest: JwtRequest): JwtResponse

    fun logout(refreshToken: String)

    fun getAccessToken(refreshToken: String?): JwtResponse

    fun refresh(refreshToken: String?): JwtResponse
}
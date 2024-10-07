package cz.cvut.fit.sp1.api.security.service.interfaces

import cz.cvut.fit.sp1.api.data.model.UserAccount
import io.jsonwebtoken.Claims

interface JwtProvider {

    fun generateAccessToken(userAccount: UserAccount): String

    fun generateRefreshToken(userAccount: UserAccount): String

    fun isAccessTokenValid(token: String?): Boolean

    fun isRefreshTokenValid(token: String?): Boolean

    fun getAccessClaims(token: String): Claims

    fun getRefreshClaims(token: String): Claims
}
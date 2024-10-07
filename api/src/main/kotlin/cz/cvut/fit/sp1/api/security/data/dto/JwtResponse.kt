package cz.cvut.fit.sp1.api.security.data.dto

import cz.cvut.fit.sp1.api.data.model.AccountRole

data class JwtResponse(
    val type: String = "Bearer",
    val accessToken: String? = null,
    val refreshToken: String? = null,
    val userId: Long? = null,
    val role: AccountRole? = null,
)

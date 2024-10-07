package cz.cvut.fit.sp1.api.security.utils

import cz.cvut.fit.sp1.api.data.model.AccountRole
import cz.cvut.fit.sp1.api.security.constants.JwtClaimsConstants
import cz.cvut.fit.sp1.api.security.data.model.JwtAuthentication
import io.jsonwebtoken.Claims

object JwtUtils {

    fun generate(claims: Claims): JwtAuthentication {
        val userId = claims.get(JwtClaimsConstants.USER_ID_KEY, Long::class.java)
        val role = claims.get(JwtClaimsConstants.USER_ROLE_KEY, AccountRole::class.java)
        return JwtAuthentication(
            userId = userId,
            email = claims.subject,
            role = role,
        )
    }
}
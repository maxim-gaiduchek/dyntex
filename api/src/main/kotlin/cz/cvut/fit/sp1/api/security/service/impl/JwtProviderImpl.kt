package cz.cvut.fit.sp1.api.security.service.impl

import cz.cvut.fit.sp1.api.data.model.UserAccount
import cz.cvut.fit.sp1.api.security.constants.JwtClaimsConstants
import cz.cvut.fit.sp1.api.security.service.interfaces.JwtProvider
import io.jsonwebtoken.*
import io.jsonwebtoken.io.Decoders
import io.jsonwebtoken.security.Keys
import lombok.extern.slf4j.Slf4j
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import java.security.Key
import java.time.LocalDateTime
import java.time.ZoneId
import java.util.*
import javax.crypto.SecretKey

@Slf4j
@Service
class JwtProviderImpl(
    @Value("\${jwt.secret.access}") jwtAccessSecret: String?,
    @Value("\${jwt.secret.refresh}") jwtRefreshSecret: String?,
    @Value("\${jwt.cookie.age.refresh}") val jwtRefreshLifetime: Long,
) : JwtProvider {

    private val logger = LoggerFactory.getLogger(javaClass)
    private val jwtAccessSecret: SecretKey = Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtAccessSecret))
    private val jwtRefreshSecret: SecretKey = Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtRefreshSecret))

    override fun generateAccessToken(userAccount: UserAccount): String {
        val now = LocalDateTime.now()
        val expirationInstant = now.plusMinutes(5).atZone(ZoneId.systemDefault()).toInstant()
        val expiration = Date.from(expirationInstant)
        return Jwts.builder()
            .setSubject(userAccount.email)
            .setExpiration(expiration)
            .signWith(jwtAccessSecret)
            .claim(JwtClaimsConstants.USER_ID_KEY, userAccount.id)
            .claim(JwtClaimsConstants.USER_ROLE_KEY, userAccount.role)
            .claim(JwtClaimsConstants.CREATED_AT, now.toString())
            .claim(JwtClaimsConstants.TYPE, "ACCESS")
            .compact()
    }

    override fun generateRefreshToken(userAccount: UserAccount): String {
        val now = LocalDateTime.now()
        val expirationInstant = now.plusSeconds(jwtRefreshLifetime)
            .atZone(ZoneId.systemDefault())
            .toInstant()
        val expiration = Date.from(expirationInstant)
        return Jwts.builder()
            .setSubject(userAccount.name)
            .setExpiration(expiration)
            .signWith(jwtRefreshSecret)
            .claim(JwtClaimsConstants.USER_ID_KEY, userAccount.id)
            .claim(JwtClaimsConstants.CREATED_AT, now.toString())
            .claim(JwtClaimsConstants.TYPE, "REFRESH")
            .compact()
    }

    override fun isAccessTokenValid(token: String?): Boolean {
        return validateToken(token, jwtAccessSecret)
    }

    private fun validateToken(token: String?, secret: Key): Boolean {
        try {
            Jwts.parserBuilder().setSigningKey(secret).build().parseClaimsJws(token)
            return true
        } catch (expEx: ExpiredJwtException) {
            logger.error("Token expired $token, ${expEx.message}")
        } catch (unsEx: UnsupportedJwtException) {
            logger.error("Unsupported jwt", unsEx)
        } catch (mjEx: MalformedJwtException) {
            logger.error("Malformed jwt", mjEx)
        } catch (e: Exception) {
            logger.error("invalid token", e)
        }
        return false
    }

    override fun isRefreshTokenValid(token: String?): Boolean {
        return validateToken(token, jwtRefreshSecret)
    }

    override fun getAccessClaims(token: String): Claims {
        return getClaims(token, jwtAccessSecret)
    }

    private fun getClaims(token: String, secret: Key): Claims {
        return Jwts.parserBuilder()
            .setSigningKey(secret)
            .build()
            .parseClaimsJws(token)
            .body
    }

    override fun getRefreshClaims(token: String): Claims {
        return getClaims(token, jwtRefreshSecret)
    }
}

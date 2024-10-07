package cz.cvut.fit.sp1.api.security.filter

import cz.cvut.fit.sp1.api.security.service.interfaces.JwtProvider
import cz.cvut.fit.sp1.api.security.utils.JwtUtils
import jakarta.servlet.FilterChain
import jakarta.servlet.ServletException
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import lombok.RequiredArgsConstructor
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.util.StringUtils
import org.springframework.web.filter.OncePerRequestFilter
import java.io.IOException

@RequiredArgsConstructor
class JwtFilter(
    private val jwtProvider: JwtProvider
) : OncePerRequestFilter() {

    companion object {
        private const val AUTHORIZATION_HEADER = "Authorization"
        private const val BEARER_HEADER = "Bearer"
    }

    @Throws(IOException::class, ServletException::class)
    public override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        val token = getTokenFromRequest(request)
        if (token != null && jwtProvider.isAccessTokenValid(token)) {
            val claims = jwtProvider.getAccessClaims(token)
            val jwtAuthentication = JwtUtils.generate(claims)
            jwtAuthentication.setAuthenticated(true)
            SecurityContextHolder.getContext().authentication = jwtAuthentication
        }
        filterChain.doFilter(request, response)
    }

    private fun getTokenFromRequest(request: HttpServletRequest): String? {
        val bearer = request.getHeader(AUTHORIZATION_HEADER)
        if (StringUtils.hasText(bearer) && bearer.startsWith("$BEARER_HEADER ")) {
            return bearer.substring(BEARER_HEADER.length + 1)
        }
        return null
    }
}
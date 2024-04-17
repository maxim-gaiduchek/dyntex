package cz.cvut.fit.sp1.api.security.filter

import cz.cvut.fit.sp1.api.data.model.UserAccount
import cz.cvut.fit.sp1.api.data.service.interfaces.UserAccountService
import cz.cvut.fit.sp1.api.security.model.TokenAuthentication
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.http.HttpHeaders
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.filter.OncePerRequestFilter

class TokenFilter(
    private var userAccountService: UserAccountService
) : OncePerRequestFilter() {

    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        val token = request.getHeader(HttpHeaders.AUTHORIZATION)
        if (token.isNullOrBlank()) {
            filterChain.doFilter(request, response)
            return
        }
        val user = userAccountService.findByToken(token)
        if (!user.isPresent) {
            filterChain.doFilter(request, response)
            return
        }
        val authentication = buildAuthentication(user.get())
        authentication.isAuthenticated = true
        SecurityContextHolder.getContext().authentication = authentication
        filterChain.doFilter(request, response)
    }

    private fun buildAuthentication(user: UserAccount): TokenAuthentication {
        return TokenAuthentication(
            userId = user.id,
            email = user.email,
            userName = user.name,
            role = user.role
        )
    }
}
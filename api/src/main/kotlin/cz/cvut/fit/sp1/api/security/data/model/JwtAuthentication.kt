package cz.cvut.fit.sp1.api.security.data.model

import cz.cvut.fit.sp1.api.data.model.AccountRole
import org.springframework.security.core.Authentication
import org.springframework.security.core.GrantedAuthority

class JwtAuthentication(
    private var authenticated: Boolean = false,
    val userId: Long,
    val email: String,
    val role: AccountRole
) : Authentication {

    override fun getName(): String {
        return email
    }

    override fun getAuthorities(): MutableCollection<out GrantedAuthority> {
        return mutableListOf(role)
    }

    override fun getCredentials(): Any? {
        return null
    }

    override fun getDetails(): Any? {
        return null
    }

    override fun getPrincipal(): Any {
        return email
    }

    override fun isAuthenticated(): Boolean {
        return authenticated
    }

    @Throws(IllegalArgumentException::class)
    override fun setAuthenticated(isAuthenticated: Boolean) {
        this.authenticated = isAuthenticated
    }
}
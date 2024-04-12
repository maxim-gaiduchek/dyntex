package cz.cvut.fit.sp1.api.security.model

import cz.cvut.fit.sp1.api.data.model.AccountRole
import org.springframework.security.core.Authentication
import org.springframework.security.core.GrantedAuthority

class TokenAuthentication(
    private var authenticated: Boolean = false,
    val userId: Long,
    val email: String,
    val userName: String,
    val role: AccountRole
) : Authentication {

    override fun getName(): String {
        return userName
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

    override fun setAuthenticated(isAuthenticated: Boolean) {
        authenticated = isAuthenticated
    }
}
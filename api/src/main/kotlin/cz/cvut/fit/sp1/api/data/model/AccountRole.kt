package cz.cvut.fit.sp1.api.data.model

import org.springframework.security.core.GrantedAuthority

enum class AccountRole : GrantedAuthority {

    ADMIN, USER;

    override fun getAuthority(): String {
        return name
    }
}

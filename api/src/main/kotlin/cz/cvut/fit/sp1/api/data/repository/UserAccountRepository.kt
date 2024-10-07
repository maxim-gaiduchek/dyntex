package cz.cvut.fit.sp1.api.data.repository

import cz.cvut.fit.sp1.api.data.model.UserAccount
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.domain.Specification
import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

interface UserAccountRepository : JpaRepository<UserAccount, Long> {

    fun findAll(specification: Specification<UserAccount>, pageable: Pageable): Page<UserAccount>

    fun getByToken(token: String): Optional<UserAccount>

    fun findByIdAndAuthEnableTrue(id: Long): Optional<UserAccount>

    fun getByTokenAndAuthEnableTrue(token: String): Optional<UserAccount>

    fun findByEmailAndPasswordAndAuthEnableTrue(email: String, password: String): Optional<UserAccount>

    fun existsByEmail(email: String): Boolean

    fun findByAuthToken(authToken: String): Optional<UserAccount>

    fun findByEmailAndAuthEnableTrue(email: String): Optional<UserAccount>

    fun findByRefreshTokensContainsAndAuthEnableTrue(refreshToken: String): UserAccount?
}
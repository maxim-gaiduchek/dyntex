package cz.cvut.fit.sp1.api.data.repository

import cz.cvut.fit.sp1.api.data.model.UserAccount
import org.springframework.data.jpa.repository.JpaRepository
import java.util.Optional

interface UserAccountRepository : JpaRepository<UserAccount, Long> {

    fun findByidAndAuthEnableTrue(id:Long):Optional<UserAccount>

    fun getByTokenAndAuthEnableTrue(token: String): Optional<UserAccount>

    fun getByEmailAndPasswordAndAuthEnableTrue(email: String, password: String): Optional<UserAccount>

    fun existsByEmail(email: String): Boolean

    fun findByAuthToken(authToken: String): Optional<UserAccount>

}
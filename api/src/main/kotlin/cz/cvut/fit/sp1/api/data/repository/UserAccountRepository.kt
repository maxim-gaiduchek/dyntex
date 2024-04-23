package cz.cvut.fit.sp1.api.data.repository

import cz.cvut.fit.sp1.api.data.model.Tag
import cz.cvut.fit.sp1.api.data.model.UserAccount
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.domain.Specification
import org.springframework.data.jpa.repository.JpaRepository
import java.util.Optional

interface UserAccountRepository : JpaRepository<UserAccount, Long> {

    fun findAll(specification: Specification<UserAccount>, pageable: Pageable): Page<UserAccount>

    fun getByToken(token: String): Optional<UserAccount>

    fun getByEmailAndPassword(email: String, password: String): Optional<UserAccount>

    fun existsByEmail(email: String): Boolean
}
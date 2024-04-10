package cz.cvut.fit.sp1.api.data.service.interfaces

import cz.cvut.fit.sp1.api.data.dto.UserRegistrationDto
import cz.cvut.fit.sp1.api.data.model.UserAccount
import java.util.*

interface UserAccountService {

    fun getById(id: Long): Optional<UserAccount>

    fun getByToken(token: String): Optional<UserAccount>

    fun getAuthenticated(): UserAccount

    fun findByIdOrThrow(id: Long): UserAccount

    fun register(userRegistrationDto: UserRegistrationDto): UserAccount
}
package cz.cvut.fit.sp1.api.data.service.interfaces

import cz.cvut.fit.sp1.api.data.dto.UserAccountDto
import cz.cvut.fit.sp1.api.data.dto.UserCredentialsDto
import cz.cvut.fit.sp1.api.data.model.UserAccount
import cz.cvut.fit.sp1.api.data.model.media.Avatar
import java.util.*

interface UserAccountService {

    fun getById(id: Long): Optional<UserAccount>

    fun getByToken(token: String): Optional<UserAccount>

    fun getAuthenticated(): UserAccount

    fun update(id: Long, userAccountDto: UserAccountDto): UserAccount

    fun updateAvatar(id: Long, avatar: Avatar): UserAccount

    fun findByIdOrThrow(id: Long): UserAccount

    fun register(userCredentialsDto: UserCredentialsDto): UserAccount

    fun login(userCredentialsDto: UserCredentialsDto): UserAccount
}
package cz.cvut.fit.sp1.api.data.service.interfaces

import cz.cvut.fit.sp1.api.data.dto.UserAccountDto
import cz.cvut.fit.sp1.api.data.dto.UserCredentialsDto
import cz.cvut.fit.sp1.api.data.model.UserAccount
import org.springframework.web.multipart.MultipartFile
import java.util.*

interface UserAccountService {

    fun findById(id: Long): Optional<UserAccount>

    fun findByToken(token: String): Optional<UserAccount>

    fun getByAuthentication(): UserAccount

    fun update(id: Long, userAccountDto: UserAccountDto): UserAccount

    fun updateAvatar(id: Long, file: MultipartFile): UserAccount

    fun getByIdOrThrow(id: Long): UserAccount

    fun register(userCredentialsDto: UserCredentialsDto): UserAccount

    fun login(userCredentialsDto: UserCredentialsDto): UserAccount
}
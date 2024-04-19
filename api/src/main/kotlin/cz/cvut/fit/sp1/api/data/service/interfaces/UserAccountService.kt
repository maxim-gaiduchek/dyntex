package cz.cvut.fit.sp1.api.data.service.interfaces

import cz.cvut.fit.sp1.api.data.dto.UserAccountDto
import cz.cvut.fit.sp1.api.data.dto.UserCredentialsDto
import cz.cvut.fit.sp1.api.data.model.UserAccount
import org.springframework.web.multipart.MultipartFile
import java.util.*

interface UserAccountService {

    fun findById(id: Long): Optional<UserAccount>

    fun getByIdOrThrow(id: Long): UserAccount

    fun findByToken(token: String): Optional<UserAccount>

    fun getAuthenticated(): UserAccount

    fun update(id: Long, userAccountDto: UserAccountDto): UserAccount

    fun updateAvatar(id: Long, file: MultipartFile): UserAccount

    fun register(userCredentialsDto: UserCredentialsDto): UserAccount

    fun login(userCredentialsDto: UserCredentialsDto): UserAccount

    fun delete(id: Long)

    fun findByAuthToken(token: String): UserAccount

    fun save(user:UserAccount):UserAccount
}
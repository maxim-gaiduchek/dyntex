package cz.cvut.fit.sp1.api.data.service.impl

import cz.cvut.fit.sp1.api.data.dto.UserRegistrationDto
import cz.cvut.fit.sp1.api.data.model.UserAccount
import cz.cvut.fit.sp1.api.data.repository.UserAccountRepository
import cz.cvut.fit.sp1.api.data.service.interfaces.UserAccountService
import cz.cvut.fit.sp1.api.exception.AccessDeniedException
import cz.cvut.fit.sp1.api.exception.EntityNotFoundException
import cz.cvut.fit.sp1.api.exception.ValidationException
import cz.cvut.fit.sp1.api.exception.exceptioncodes.UserAccountExceptionCodes
import cz.cvut.fit.sp1.api.security.model.TokenAuthentication
import org.apache.commons.lang3.RandomStringUtils
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Service
import java.util.*

@Service
class UserAccountServiceImpl(
    private val userAccountRepository: UserAccountRepository
) : UserAccountService {

    companion object {
        private const val EMPTY_STRING_HASH = "e3b0c44298fc1c149afbf4c8996fb92427ae41e4649b934ca495991b7852b855"
        private const val TOKEN_SIZE = 128
    }

    override fun getById(id: Long): Optional<UserAccount> {
        return userAccountRepository.findById(id)
    }

    override fun getByToken(token: String): Optional<UserAccount> {
        return userAccountRepository.getByToken(token)
    }

    override fun getAuthenticated(): UserAccount {
        val id = fetchUserIdFromAuthentication()
        return findByIdOrThrow(id)
    }

    private fun fetchUserIdFromAuthentication(): Long {
        val auth = SecurityContextHolder.getContext().authentication as? TokenAuthentication
        auth ?: throw AccessDeniedException(UserAccountExceptionCodes.USER_ACCESS_DENIED)
        return auth.userId
    }

    override fun findByIdOrThrow(id: Long): UserAccount {
        return getById(id)
            .orElseThrow { EntityNotFoundException(UserAccountExceptionCodes.USER_NOT_FOUND, id) }
    }

    override fun register(userRegistrationDto: UserRegistrationDto): UserAccount {
        checkUserAccountCreationPossibility(userRegistrationDto)
        val user = buildNewUser(userRegistrationDto)
        return userAccountRepository.save(user)
    }

    private fun checkUserAccountCreationPossibility(userRegistrationDto: UserRegistrationDto) {
        val email = userRegistrationDto.email
        if (userAccountRepository.existsByEmail(email)) {
            throw ValidationException(UserAccountExceptionCodes.USER_EMAIL_ALREADY_EXISTS, email)
        }
        val password = userRegistrationDto.password
        if (EMPTY_STRING_HASH == password) {
            throw ValidationException(UserAccountExceptionCodes.USER_PASSWORD_IS_EMPTY)
        }
    }

    private fun buildNewUser(userRegistrationDto: UserRegistrationDto): UserAccount {
        val token = RandomStringUtils.random(TOKEN_SIZE, true, false)
        return UserAccount(
            name = userRegistrationDto.name,
            email = userRegistrationDto.email,
            password = userRegistrationDto.password,
            token = token
        )
    }
}
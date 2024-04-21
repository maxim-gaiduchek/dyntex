package cz.cvut.fit.sp1.api.data.service.impl

import cz.cvut.fit.sp1.api.component.mapper.UserAccountMapper
import cz.cvut.fit.sp1.api.data.dto.UserAccountDto
import cz.cvut.fit.sp1.api.data.dto.UserCredentialsDto
import cz.cvut.fit.sp1.api.data.dto.search.SearchTagDto
import cz.cvut.fit.sp1.api.data.dto.search.SearchUserAccountDto
import cz.cvut.fit.sp1.api.data.dto.search.SearchUserAccountParamsDto
import cz.cvut.fit.sp1.api.data.model.UserAccount
import cz.cvut.fit.sp1.api.data.repository.UserAccountRepository
import cz.cvut.fit.sp1.api.data.service.interfaces.AvatarService
import cz.cvut.fit.sp1.api.data.service.interfaces.UserAccountService
import cz.cvut.fit.sp1.api.exception.AccessDeniedException
import cz.cvut.fit.sp1.api.exception.EntityNotFoundException
import cz.cvut.fit.sp1.api.exception.ValidationException
import cz.cvut.fit.sp1.api.exception.exceptioncodes.UserAccountExceptionCodes
import cz.cvut.fit.sp1.api.security.service.interfaces.SecurityProvider
import org.apache.commons.lang3.RandomStringUtils
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile
import java.util.*
import kotlin.jvm.optionals.getOrElse

@Service
class UserAccountServiceImpl(
    private val userAccountRepository: UserAccountRepository,
    private val userAccountMapper: UserAccountMapper,
    private val avatarService: AvatarService,
    private val securityProvider: SecurityProvider,
) : UserAccountService {
    companion object {
        private const val EMPTY_STRING_HASH = "e3b0c44298fc1c149afbf4c8996fb92427ae41e4649b934ca495991b7852b855"
        private const val TOKEN_SIZE = 128
    }

    override fun findAll(paramsDto: SearchUserAccountParamsDto?): SearchUserAccountDto? {
        if (paramsDto == null) {
            return null
        }
        val specification = paramsDto.buildSpecification()
        val pageable = paramsDto.buildPageable()
        val page = userAccountRepository.findAll(specification, pageable)
        val userAccounts =
            page.content.stream()
                .map { userAccountMapper.toDto(it)!! }
                .toList()
        return SearchUserAccountDto(
            userAccounts = userAccounts,
            currentPage = page.number + 1,
            totalPages = page.totalPages,
            totalMatches = page.totalElements,
        )
    }

    override fun findById(id: Long): Optional<UserAccount> {
        return userAccountRepository.findById(id)
    }

    override fun findByToken(token: String): Optional<UserAccount> {
        return userAccountRepository.getByToken(token)
    }

    override fun getByAuthentication(): UserAccount {
        val id = fetchUserIdFromAuthentication()
        return getByIdOrThrow(id)
    }

    private fun fetchUserIdFromAuthentication(): Long {
        val id = securityProvider.fetchAuthenticatedUserId()
        id ?: throw AccessDeniedException(UserAccountExceptionCodes.USER_ACCESS_DENIED)
        return id
    }

    override fun getByIdOrThrow(id: Long): UserAccount {
        return findById(id)
            .getOrElse {
                throw EntityNotFoundException(UserAccountExceptionCodes.USER_NOT_FOUND, id)
            }
    }

    override fun update(id: Long, userAccountDto: UserAccountDto): UserAccount {
        val user = getByIdOrThrow(id)
        user.name = userAccountDto.name!!
        return userAccountRepository.save(user)
    }

    override fun updateAvatar(id: Long, file: MultipartFile): UserAccount {
        val user = getByIdOrThrow(id)
        if (user.avatar != null) {
            avatarService.delete(user.avatar!!)
        }
        val avatar = avatarService.save(id, file)
        user.avatar = avatar
        avatar?.userAccount = user
        return userAccountRepository.save(user)
    }

    override fun register(userCredentialsDto: UserCredentialsDto): UserAccount {
        checkUserAccountCreationPossibility(userCredentialsDto)
        val user = buildNewUser(userCredentialsDto)
        return userAccountRepository.save(user)
    }

    private fun checkUserAccountCreationPossibility(userCredentialsDto: UserCredentialsDto) {
        val password = userCredentialsDto.password
        if (EMPTY_STRING_HASH == password) {
            throw ValidationException(UserAccountExceptionCodes.USER_PASSWORD_IS_EMPTY)
        }
        val email = userCredentialsDto.email!!
        if (userAccountRepository.existsByEmail(email)) {
            throw ValidationException(UserAccountExceptionCodes.USER_EMAIL_ALREADY_EXISTS, email)
        }
    }

    private fun buildNewUser(userCredentialsDto: UserCredentialsDto): UserAccount {
        val token = RandomStringUtils.random(TOKEN_SIZE, true, false)
        return UserAccount(
            name = userCredentialsDto.name!!,
            email = userCredentialsDto.email!!,
            password = userCredentialsDto.password!!,
            token = token,
        )
    }

    override fun login(userCredentialsDto: UserCredentialsDto): UserAccount {
        val user =
            userAccountRepository.getByEmailAndPassword(userCredentialsDto.email!!, userCredentialsDto.password!!)
                .orElseThrow { AccessDeniedException(UserAccountExceptionCodes.USER_ACCESS_DENIED) }
        return user!!
    }
}

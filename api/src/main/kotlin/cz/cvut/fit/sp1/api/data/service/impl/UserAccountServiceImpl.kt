package cz.cvut.fit.sp1.api.data.service.impl

import cz.cvut.fit.sp1.api.component.mapper.UserAccountMapper
import cz.cvut.fit.sp1.api.data.dto.UserAccountDto
import cz.cvut.fit.sp1.api.data.dto.UserCredentialsDto
import cz.cvut.fit.sp1.api.data.dto.search.SearchUserAccountDto
import cz.cvut.fit.sp1.api.data.dto.search.SearchUserAccountParamsDto
import cz.cvut.fit.sp1.api.data.model.UserAccount
import cz.cvut.fit.sp1.api.data.repository.UserAccountRepository
import cz.cvut.fit.sp1.api.data.service.interfaces.AuthService
import cz.cvut.fit.sp1.api.data.service.interfaces.AvatarService
import cz.cvut.fit.sp1.api.data.service.interfaces.EmailService
import cz.cvut.fit.sp1.api.data.service.interfaces.UserAccountService
import cz.cvut.fit.sp1.api.exception.AccessDeniedException
import cz.cvut.fit.sp1.api.exception.EntityNotFoundException
import cz.cvut.fit.sp1.api.exception.ValidationException
import cz.cvut.fit.sp1.api.exception.exceptioncodes.UserAccountExceptionCodes
import cz.cvut.fit.sp1.api.security.service.interfaces.SecurityProvider
import jakarta.transaction.Transactional
import org.apache.commons.lang3.RandomStringUtils
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Lazy
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile
import org.thymeleaf.TemplateEngine
import org.thymeleaf.context.Context
import java.util.*
import kotlin.jvm.optionals.getOrElse

@Service
class UserAccountServiceImpl(
    private val userAccountRepository: UserAccountRepository,
    private val userAccountMapper: UserAccountMapper,
    private val avatarService: AvatarService,
    private val securityProvider: SecurityProvider,
    @Value("\${verification.enable}") private val verificationEnable: Boolean,
    private var emailService: EmailService,
    @Value("\${verification.mail.url}") private val mailUrl: String,
    private val templateEngine: TemplateEngine,
    @Value("\${verification.token-expiring-time}") private val tokenExpire: Int,
) : UserAccountService {
    @Autowired
    @Lazy
    private var authService: AuthService? = null


    companion object {
        private const val EMPTY_STRING_HASH = "e3b0c44298fc1c149afbf4c8996fb92427ae41e4649b934ca495991b7852b855"
        private const val TOKEN_SIZE = 128
        private const val AUTH_TOKEN_SIZE = 128

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

    override fun findByIdAuthEnableTrue(id: Long): Optional<UserAccount> {
        return userAccountRepository.findByIdAndAuthEnableTrue(id)
    }

    override fun findByTokenAndAuthEnableTrue(token: String): Optional<UserAccount> {
        return userAccountRepository.getByTokenAndAuthEnableTrue(token)
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
        val user = getByIdAndAuthEnableTrueOrThrow(id)
        user.name = userAccountDto.name!!
        return userAccountRepository.save(user)
    }

    override fun updateAvatar(id: Long, file: MultipartFile): UserAccount {
        val user = getByIdAndAuthEnableTrueOrThrow(id)
        if (user.avatar != null) {
            avatarService.delete(user.avatar!!)
        }
        val avatar = avatarService.save(id, file)
        user.avatar = avatar
        avatar?.userAccount = user
        return userAccountRepository.save(user)
    }

    @Transactional
    override fun register(userCredentialsDto: UserCredentialsDto): UserAccount {
        checkUserAccountCreationPossibility(userCredentialsDto)
        val user = buildNewUser(userCredentialsDto)
        if (!verificationEnable) {
            user.authEnable = true
            return userAccountRepository.save(user)
        }
        authService!!.sendVerificationEmail(user.email, user.authToken)
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
        val authToken = RandomStringUtils.random(AUTH_TOKEN_SIZE, true, false)
        return UserAccount(
            name = userCredentialsDto.name!!,
            email = userCredentialsDto.email!!,
            password = userCredentialsDto.password!!,
            token = token,
            authToken = authToken,
            dateOfRecovery = null
        )
    }

    override fun login(userCredentialsDto: UserCredentialsDto): UserAccount {
        val user =
            userAccountRepository.findByEmailAndPasswordAndAuthEnableTrue(
                userCredentialsDto.email!!,
                userCredentialsDto.password!!
            )
                .orElseThrow { AccessDeniedException(UserAccountExceptionCodes.USER_ACCESS_DENIED) }
        return user!!
    }

    override fun countAll(): Long {
        return userAccountRepository.count()
    }

    override fun delete(id: Long) {
        val user = getByIdOrThrow(id)
        userAccountRepository.delete(user)
    }

    override fun getByAuthToken(token: String): UserAccount {
        return userAccountRepository.findByAuthToken(token)
            .orElseThrow { AccessDeniedException(UserAccountExceptionCodes.USER_ACCESS_DENIED) }
    }

    override fun save(user: UserAccount): UserAccount {
        return userAccountRepository.save(user)
    }

    override fun getByIdAndAuthEnableTrueOrThrow(id: Long): UserAccount {
        return findByIdAuthEnableTrue(id)
            .getOrElse {
                throw EntityNotFoundException(UserAccountExceptionCodes.USER_NOT_FOUND, id)
            }
    }

    override fun getByEmail(email: String): UserAccount {
        return userAccountRepository.findByEmailAndAuthEnableTrue(email)
            .getOrElse {
                throw EntityNotFoundException(UserAccountExceptionCodes.USER_NOT_FOUND, email)
            }
    }

//    override fun recoveryPassword(email: String) {
//        val user = getByEmail(email)
//        val authToken = RandomStringUtils.random(AUTH_TOKEN_SIZE, true, false)
//        user.authToken = authToken
//        user.dateOfRecovery!!.time = Date().time
//        save(user)
//        val confirmationUrl = "$mailUrl/recovery?t=$authToken"
//        val context = Context().apply {
//            setVariable("confirmationUrl", confirmationUrl)
//        }
//        val emailContent = templateEngine.process("verification_email", context)
//        emailService.sendEmail(email, "Verify your email", emailContent)
//    }

    override fun recoveryPassword(email: String) {
        val user = getByEmail(email)
        val authToken = RandomStringUtils.random(AUTH_TOKEN_SIZE, true, false)

        val currentDate = Date()
        user.dateOfRecovery = user.dateOfRecovery?.apply { time = currentDate.time } ?: currentDate
//TODO NU UZNAI BLYAT
        user.authToken = authToken
        save(user)
        val confirmationUrl = "$mailUrl/recovery?t=$authToken"
        val emailContent = getEmailContent(confirmationUrl)
        emailService.sendEmail(email, "Verify your email", emailContent)
    }

    private fun getEmailContent(confirmationUrl: String): String {
        val context = Context().apply {
            setVariable("confirmationUrl", confirmationUrl)
        }
        return templateEngine.process("verification_email", context)
    }


    override fun updatePassword(
        authToken: String,
        password: String
    ) {
        val user = getByAuthToken(authToken)
        if (expiringCheck(user)) {
            val token = RandomStringUtils.random(AUTH_TOKEN_SIZE, true, false)
            user.token = token
            user.password = password
            save(user)
            return
        }
        throw ValidationException(UserAccountExceptionCodes.AUTH_TOKEN_IS_EXPIRED)
    }

    private fun expiringCheck(user: UserAccount): Boolean {
        return Date().time <= user.dateOfRecovery!!.time + (tokenExpire * 60 * 1000)
    }

}

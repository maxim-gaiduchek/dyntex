package cz.cvut.fit.sp1.api.controller

import cz.cvut.fit.sp1.api.component.mapper.UserAccountMapper
import cz.cvut.fit.sp1.api.data.dto.UserAccountDto
import cz.cvut.fit.sp1.api.data.dto.UserCredentialsDto
import cz.cvut.fit.sp1.api.data.service.interfaces.UserAccountService
import cz.cvut.fit.sp1.api.utils.CookieUtils
import cz.cvut.fit.sp1.api.validation.group.UserLoginGroup
import cz.cvut.fit.sp1.api.validation.group.UserRegistrationGroup
import jakarta.servlet.http.HttpServletResponse
import org.springframework.http.HttpHeaders
import org.springframework.http.ResponseCookie
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/users")
@CrossOrigin(origins = ["*"])
class UserAccountController(
    private var userAccountService: UserAccountService,
    private var userAccountMapper: UserAccountMapper
) {

    companion object {
        private const val TOKEN_COOKIE_NAME = "token"
        private const val TOKEN_COOKIE_AGE = 259200L // 3 days
    }

    @GetMapping
    fun getAuthenticated(): UserAccountDto {
        val user = userAccountService.getAuthenticated()
        return userAccountMapper.toDto(user)
    }

    @GetMapping("/{id}")
    fun getById(@PathVariable id: Long): UserAccountDto {
        val user = userAccountService.findByIdOrThrow(id)
        return userAccountMapper.toDto(user)
    }

    @PostMapping
    fun register(@Validated(UserRegistrationGroup::class) userCredentialsDto: UserCredentialsDto): UserAccountDto {
        val user = userAccountService.register(userCredentialsDto)
        return userAccountMapper.toDto(user)
    }

    @PostMapping
    fun login(
        @Validated(UserLoginGroup::class) userCredentialsDto: UserCredentialsDto,
        response: HttpServletResponse
    ): UserAccountDto {
        val user = userAccountService.login(userCredentialsDto)
        val tokenCookie = createRefreshTokenCookie(user.token)
        response.addHeader(HttpHeaders.SET_COOKIE, tokenCookie.toString())
        return userAccountMapper.toDto(user)
    }

    private fun createRefreshTokenCookie(token: String): ResponseCookie {
        return CookieUtils.createHttpOnlyCookie(TOKEN_COOKIE_NAME, token, "/", TOKEN_COOKIE_AGE)
    }
}
package cz.cvut.fit.sp1.api.controller

import cz.cvut.fit.sp1.api.component.mapper.UserAccountMapper
import cz.cvut.fit.sp1.api.data.dto.UserAccountDto
import cz.cvut.fit.sp1.api.data.dto.UserCredentialsDto
import cz.cvut.fit.sp1.api.data.service.interfaces.UserAccountService
import cz.cvut.fit.sp1.api.utils.CookieUtils
import cz.cvut.fit.sp1.api.validation.group.UserLoginGroup
import cz.cvut.fit.sp1.api.validation.group.UserRegistrationGroup
import jakarta.annotation.security.RolesAllowed
import jakarta.servlet.http.HttpServletResponse
import org.springframework.http.HttpHeaders
import org.springframework.http.ResponseCookie
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/users")
class UserAccountController(
    private var userAccountService: UserAccountService,
    private var userAccountMapper: UserAccountMapper,
) {
    companion object {
        private const val TOKEN_COOKIE_NAME = "token"
        private const val TOKEN_COOKIE_AGE = 259200L // 3 days
    }

    @GetMapping("/authenticated")
    @RolesAllowed("USER", "ADMIN")
    fun getAuthenticated(): UserAccountDto {
        val user = userAccountService.getAuthenticated()
        return userAccountMapper.toDto(user)
    }

    @GetMapping("/{id}")
    fun getById(
        @PathVariable id: Long,
    ): UserAccountDto {
        val user = userAccountService.getByIdOrThrow(id)
        return userAccountMapper.toDto(user)
    }

    @PostMapping
    fun register(
        @Validated(UserRegistrationGroup::class) @RequestBody userCredentialsDto: UserCredentialsDto,
    ): UserAccountDto {
        val user = userAccountService.register(userCredentialsDto)
        return userAccountMapper.toDto(user)
    }

    @PostMapping("/login")
    fun login(
        @Validated(UserLoginGroup::class) @RequestBody userCredentialsDto: UserCredentialsDto,
        response: HttpServletResponse,
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

package cz.cvut.fit.sp1.api.controller

import cz.cvut.fit.sp1.api.component.mapper.UserAccountMapper
import cz.cvut.fit.sp1.api.data.dto.UserAccountDto
import cz.cvut.fit.sp1.api.data.dto.UserRegistrationDto
import cz.cvut.fit.sp1.api.data.service.interfaces.UserAccountService
import jakarta.validation.Valid
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RestController

@RestController("/users")
class UserAccountController(
    private var userAccountService: UserAccountService,
    private var userAccountMapper: UserAccountMapper
) {

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
    fun register(@Valid userRegistrationDto: UserRegistrationDto): UserAccountDto {
        val user = userAccountService.register(userRegistrationDto)
        return userAccountMapper.toDto(user)
    }
}
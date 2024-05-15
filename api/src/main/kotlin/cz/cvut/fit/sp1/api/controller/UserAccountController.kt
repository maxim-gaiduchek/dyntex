package cz.cvut.fit.sp1.api.controller

import cz.cvut.fit.sp1.api.component.mapper.UserAccountMapper
import cz.cvut.fit.sp1.api.data.dto.UserAccountDto
import cz.cvut.fit.sp1.api.data.dto.UserCredentialsDto
import cz.cvut.fit.sp1.api.data.dto.search.SearchUserAccountDto
import cz.cvut.fit.sp1.api.data.dto.search.SearchUserAccountParamsDto
import cz.cvut.fit.sp1.api.data.service.interfaces.UserAccountService
import cz.cvut.fit.sp1.api.validation.group.UpdateGroup
import cz.cvut.fit.sp1.api.validation.group.UserLoginGroup
import cz.cvut.fit.sp1.api.validation.group.UserRegistrationGroup
import jakarta.servlet.http.HttpServletResponse
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.security.access.annotation.Secured
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile

@RestController
@RequestMapping("/users")
class UserAccountController(
    private var userAccountService: UserAccountService,
    private var userAccountMapper: UserAccountMapper,
) {
    @GetMapping
    fun getAll(
        @ModelAttribute paramsDto: SearchUserAccountParamsDto?,
    ): SearchUserAccountDto? {
        return userAccountService.findAll(paramsDto)
    }

    @GetMapping("/authenticated")
    @Secured("USER", "ADMIN")
    fun getByAuthentication(): UserAccountDto? {
        val user = userAccountService.getByAuthentication()
        return userAccountMapper.toDto(user)
    }

    @GetMapping("/{id}")
    fun getById(
        @PathVariable id: Long,
    ): UserAccountDto? {
        val user = userAccountService.getByIdOrThrow(id)
        return userAccountMapper.toDto(user)
    }

    @PostMapping
    fun register(
        @Validated(UserRegistrationGroup::class) @RequestBody userCredentialsDto: UserCredentialsDto,
        response: HttpServletResponse,
    ): UserAccountDto? {
        val user = userAccountService.register(userCredentialsDto)
        return userAccountMapper.toDtoWithToken(user)
    }

    @PostMapping("/login")
    fun login(
        @Validated(UserLoginGroup::class) @RequestBody userCredentialsDto: UserCredentialsDto,
        response: HttpServletResponse,
    ): UserAccountDto? {
        val user = userAccountService.login(userCredentialsDto)
        return userAccountMapper.toDtoWithToken(user)
    }

    @PutMapping("/{id}")
    @PreAuthorize("@accessService.hasUserAccessToUpdateUser(#id)")
    fun update(
        @PathVariable id: Long,
        @Validated(UpdateGroup::class) @RequestBody userAccountDto: UserAccountDto
    ): UserAccountDto? {
        val user = userAccountService.update(id, userAccountDto)
        return userAccountMapper.toDto(user)
    }

    @PutMapping("/{id}/avatars", consumes = [MediaType.MULTIPART_FORM_DATA_VALUE])
    @PreAuthorize("@accessService.hasUserAccessToUpdateUser(#id)")
    fun updateAvatar(
        @PathVariable id: Long,
        @RequestPart("avatar", required = false) avatarFile: MultipartFile
    ): UserAccountDto? {
        val user = userAccountService.updateAvatar(id, avatarFile)
        return userAccountMapper.toDto(user)
    }

    @PutMapping("/request")
    fun recoveryRequest( @RequestBody userCredentialsDto: UserCredentialsDto): ResponseEntity<Any> {

        userAccountService.recoveryPassword(userCredentialsDto.email!!)
        return ResponseEntity(HttpStatus.OK)
    }
}

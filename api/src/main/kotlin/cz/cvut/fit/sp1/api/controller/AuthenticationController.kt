package cz.cvut.fit.sp1.api.controller

import cz.cvut.fit.sp1.api.data.dto.UserCredentialsDto
import cz.cvut.fit.sp1.api.data.service.interfaces.AuthService
import cz.cvut.fit.sp1.api.data.service.interfaces.UserAccountService
import cz.cvut.fit.sp1.api.validation.group.UserRecoveryUpdateGroup
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/auth")
class AuthenticationController(
    private val authService: AuthService,
    private val userAccountService: UserAccountService
) {

    @GetMapping("/verify")
    fun authentication(@RequestParam authToken: String): ResponseEntity<Any> {
        authService.verifyToken(authToken)
        return ResponseEntity(HttpStatus.OK)
    }

    @PostMapping("/recovery")
    fun recovery(
        @Validated(UserRecoveryUpdateGroup::class) @RequestBody userCredentialsDto: UserCredentialsDto
    ): ResponseEntity<Any> {
        userAccountService.updatePassword(userCredentialsDto.authToken!!, userCredentialsDto.password!!)
        return ResponseEntity(HttpStatus.OK)
    }
}
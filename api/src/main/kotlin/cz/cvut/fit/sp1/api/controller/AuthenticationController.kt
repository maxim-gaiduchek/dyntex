package cz.cvut.fit.sp1.api.controller

import cz.cvut.fit.sp1.api.data.dto.UserCredentialsDto
import cz.cvut.fit.sp1.api.data.service.interfaces.AuthService
import cz.cvut.fit.sp1.api.data.service.interfaces.UserAccountService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
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
        @RequestBody userCreditialsDto: UserCredentialsDto
    ): ResponseEntity<Any> {
        userAccountService.updatePassword(userCreditialsDto.authToken!!, userCreditialsDto.password!!)
        return ResponseEntity(HttpStatus.OK)
    }
}
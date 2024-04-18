package cz.cvut.fit.sp1.api.controller

import cz.cvut.fit.sp1.api.data.service.interfaces.VerificationService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/auth")
class AuthenticationController(
        private val verificationService: VerificationService
) {

    @GetMapping("/verify")
    fun authentication(@RequestParam authToken: String):ResponseEntity<Any> {
        verificationService.verifyToken(authToken)
        return ResponseEntity.ok().build()
    }
}
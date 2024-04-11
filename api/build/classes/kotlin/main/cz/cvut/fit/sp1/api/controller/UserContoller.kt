package cz.cvut.fit.sp1.api.controller

class UserContoller {
    private val userService: cz.cvut.fit.sp1.api.data.service.UserService /* compiled code */

    @org.springframework.web.bind.annotation.PostMapping
    fun upload(user: org.springframework.web.multipart.MultipartFile): kotlin.Unit { /* compiled code */ }
}

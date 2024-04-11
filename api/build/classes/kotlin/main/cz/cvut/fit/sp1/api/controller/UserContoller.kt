package cz.cvut.fit.sp1.api.controller

class UserContoller {
    private val userService: cz.cvut.fit.sp1.api.data.service.UserService /* compiled code */

    @org.springframework.web.bind.annotation.PostMapping
    fun upload(user: org.springframework.web.multipart.MultipartFile): kotlin.Unit { /* compiled code */ }
}

//package cz.cvut.fit.sp1.api.controller
//
//import org.springframework.web.bind.annotation.*
//import org.springframework.web.multipart.MultipartFile
//import cz.cvut.fit.sp1.api.data.service.UserService
//import cz.cvut.fit.sp1.api.data.model.media.Mask
//
//@RestController
//@RequestMapping("/users")
//class UserController(private val userService: UserService) {
//
//    @PostMapping
//    fun createMask(@RequestParam mask: MultipartFile): Mask {
//        return userService.create(mask)
//    }
//
//    @GetMapping("/{id}")
//    fun getMask(@PathVariable id: Long): Mask {
//        return userService.get(id)
//    }
//
//    @DeleteMapping("/{id}")
//    fun deleteMask(@PathVariable id: Long): ResponseEntity<Any> {
//        val isDeleted = userService.delete(id)
//        return if (isDeleted) {
//            ResponseEntity.ok().build()
//        } else {
//            ResponseEntity.status(HttpStatus.NOT_FOUND).build()
//        }
//    }
//}

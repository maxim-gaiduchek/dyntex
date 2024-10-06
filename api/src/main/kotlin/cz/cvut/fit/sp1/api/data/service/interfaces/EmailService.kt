package cz.cvut.fit.sp1.api.data.service.interfaces

fun interface EmailService {

    fun sendEmail(receiver: String, subject: String, text: String)
}
package cz.cvut.fit.sp1.api.data.service.interfaces

interface EmailService {

    fun sendEmail(reciever: String, subject: String, text: String)
}
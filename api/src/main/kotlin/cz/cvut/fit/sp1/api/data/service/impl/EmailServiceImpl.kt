package cz.cvut.fit.sp1.api.data.service.impl

import cz.cvut.fit.sp1.api.data.service.interfaces.EmailService
import org.springframework.mail.SimpleMailMessage
import org.springframework.mail.javamail.JavaMailSender
import org.springframework.stereotype.Service

@Service
class EmailServiceImpl(val mailSender : JavaMailSender) : EmailService {
    override fun sendEmail(receiver: String, subject: String, text: String) {
        val message = SimpleMailMessage()
        message.setTo(receiver)
        message.setSubject(subject)
        message.setText(text)
        mailSender.send(message)
    }
}
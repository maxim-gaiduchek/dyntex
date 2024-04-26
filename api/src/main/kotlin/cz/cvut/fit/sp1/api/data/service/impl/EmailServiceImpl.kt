package cz.cvut.fit.sp1.api.data.service.impl

import cz.cvut.fit.sp1.api.data.service.interfaces.EmailService
import org.springframework.mail.javamail.JavaMailSender
import org.springframework.mail.javamail.MimeMessageHelper
import org.springframework.scheduling.annotation.Async
import org.springframework.stereotype.Service

@Service
class EmailServiceImpl(val mailSender: JavaMailSender) : EmailService {

    @Async
    override fun sendEmail(receiver: String, subject: String, text: String) {
        val message = mailSender.createMimeMessage()
        val helper = MimeMessageHelper(message, true)
        helper.setTo(receiver)
        helper.setSubject(subject)
        helper.setText(text, true)
        mailSender.send(message)
    }
}
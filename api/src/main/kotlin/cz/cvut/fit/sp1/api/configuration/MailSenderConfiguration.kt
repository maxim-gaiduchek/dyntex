package cz.cvut.fit.sp1.api.configuration

import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.mail.javamail.JavaMailSender
import org.springframework.mail.javamail.JavaMailSenderImpl

@Configuration
class MailSenderConfiguration {
    @Bean
    fun mailSender(
            @Value("\${spring.mail.host}") mailHost: String,
            @Value("\${spring.mail.port}") mailPort: Int,
            @Value("\${spring.mail.username}") mailUsername: String,
            @Value("\${spring.mail.password}") mailPassword: String,
            @Value("\${spring.mail.properties.mail.smtp.auth}") mailAuth: Boolean,
            @Value("\${spring.mail.properties.mail.smtp.starttls.enable}") mailStartTLs: Boolean
    ): JavaMailSender {
        return JavaMailSenderImpl().apply {
            host = mailHost
            port = mailPort
            username = mailUsername
            password = mailPassword
            javaMailProperties.apply {
                put("mail.transport.protocol", "smtp")
                put("mail.smtp.auth", mailAuth.toString())
                put("mail.smtp.starttls.enable", mailStartTLs.toString())
                put("mail.debug", "false")
            }
        }
    }
}
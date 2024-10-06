package cz.cvut.fit.sp1.api

import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.mail.javamail.JavaMailSender

@SpringBootTest
class ApiApplicationTests {

    @MockBean
    lateinit var mailSender: JavaMailSender

    @Test
    fun contextLoads() {
        // empty on purpose =)
    }
}

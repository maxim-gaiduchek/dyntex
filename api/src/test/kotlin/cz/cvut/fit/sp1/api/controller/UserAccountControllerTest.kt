package cz.cvut.fit.sp1.api.controller

import cz.cvut.fit.sp1.api.component.mapper.UserAccountMapper
import cz.cvut.fit.sp1.api.data.dto.UserCredentialsDto
import cz.cvut.fit.sp1.api.data.service.interfaces.UserAccountService
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.mail.javamail.JavaMailSender
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@SpringBootTest
@AutoConfigureMockMvc
class UserAccountControllerTest {
    @Autowired
    lateinit var mockMvc: MockMvc

    @Autowired
    lateinit var userAccountService: UserAccountService

    @Autowired
    lateinit var userAccountMapper: UserAccountMapper

    @MockBean
    lateinit var javaMailSender: JavaMailSender

    @Test
    fun `test getAll`() {
        val user = userAccountService.register(UserCredentialsDto("name", "email123124", "password", authToken = null))

        mockMvc.perform(
            MockMvcRequestBuilders.get("/users"),
        )
            .andExpect(
                status().isOk(),
            )
            .andExpect(MockMvcResultMatchers.jsonPath("$.userAccounts").isArray())
            .andExpect(MockMvcResultMatchers.jsonPath("$.userAccounts[0].name").value(user.name))
    }
}

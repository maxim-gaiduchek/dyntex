package cz.cvut.fit.sp1.api.security.controller

import com.fasterxml.jackson.databind.ObjectMapper
import cz.cvut.fit.sp1.api.data.UserAccountTestData
import cz.cvut.fit.sp1.api.data.dto.UserCredentialsDto
import cz.cvut.fit.sp1.api.data.service.interfaces.AuthService
import cz.cvut.fit.sp1.api.data.service.interfaces.UserAccountService
import cz.cvut.fit.sp1.api.security.data.dto.JwtRequest
import cz.cvut.fit.sp1.api.security.data.dto.JwtResponse
import jakarta.servlet.http.Cookie
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.Mockito.doNothing
import org.mockito.Mockito.`when`
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.http.MediaType
import org.springframework.mail.javamail.JavaMailSender
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@SpringBootTest
@AutoConfigureMockMvc
class AuthControllerTest {

    @Autowired
    private lateinit var mockMvc: MockMvc

    @Autowired
    private lateinit var authService: AuthService

    @Autowired
    private lateinit var userAccountService: UserAccountService

    @MockBean
    lateinit var javaMailSender: JavaMailSender

    private lateinit var objectMapper: ObjectMapper

    @BeforeEach
    fun setUp() {
        objectMapper = ObjectMapper()
    }

    @Test
    fun `login should return JWT tokens and user info`() {
        val user = UserAccountTestData.defaultUser0()
        val userCredentialsDto = UserCredentialsDto(
            name = user.name,
            email = user.email,
            password = user.password,
            authToken = user.authToken,
        )
        val savedUser = userAccountService.register(userCredentialsDto)
        val jwtRequest = JwtRequest(
            login = savedUser.email,
            password = savedUser.password
        )

        `when`(authService.login(jwtRequest))
            .then { }

        mockMvc.perform(
            MockMvcRequestBuilders.post("/security/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(jwtRequest))
        )
            .andExpect(status().isOk)
            .andExpect(MockMvcResultMatchers.jsonPath("$.accessToken").value("access-token"))
            .andExpect(MockMvcResultMatchers.jsonPath("$.refreshToken").value("refresh-token"))
    }

    @Test
    fun `logout should delete refresh token cookie`() {
        val user = UserAccountTestData.defaultUser0()
        val userCredentialsDto = UserCredentialsDto(
            name = user.name,
            email = user.email,
            password = user.password,
            authToken = user.authToken,
        )
        val savedUser = userAccountService.register(userCredentialsDto)
        val jwtRequest = JwtRequest(
            login = savedUser.email,
            password = savedUser.password
        )
        val jwtResponse0 = authService.login(jwtRequest)

        doNothing()
            .`when`(authService)
            .logout(jwtResponse0.refreshToken!!)

        val loginUser0 = userAccountService.getByIdOrThrow(jwtResponse0.userId!!)
        assert(!loginUser0.refreshTokens.contains(jwtResponse0.refreshToken))

        val jwtResponse1 = authService.login(jwtRequest)
        val refreshTokenCookie = Cookie("refreshToken", jwtResponse1.refreshToken)
        mockMvc.perform(
            MockMvcRequestBuilders.get("/security/logout")
                .cookie(refreshTokenCookie)
        )
            .andExpect(status().isOk)
        val loginUser1 = userAccountService.getByIdOrThrow(jwtResponse1.userId!!)
        assert(!loginUser1.refreshTokens.contains(jwtResponse1.refreshToken))
    }

    @Test
    fun `getNewAccessToken should return new JWT access token`() {
        val response = JwtResponse("new-access-token", null)
        val refreshTokenCookie = Cookie("refreshToken", "valid-refresh-token")

        `when`(authService.getAccessToken("valid-refresh-token"))
            .thenReturn(response)

        mockMvc.perform(
            MockMvcRequestBuilders.get("/security/access")
                .cookie(refreshTokenCookie)
        )
            .andExpect(status().isOk)
            .andExpect(MockMvcResultMatchers.jsonPath("$.accessToken").value("new-access-token"))
    }

    @Test
    fun `refreshAccessToken should return refreshed JWT refresh token`() {
        val response = JwtResponse("new-access-token", "new-refresh-token")
        val refreshTokenCookie = Cookie("refreshToken", "valid-refresh-token")

        `when`(authService.refresh("valid-refresh-token"))
            .thenReturn(response)

        mockMvc.perform(
            MockMvcRequestBuilders.get("/security/refresh")
                .cookie(refreshTokenCookie)
        )
            .andExpect(status().isOk)
            .andExpect(MockMvcResultMatchers.jsonPath("$.accessToken").value("new-access-token"))
            .andExpect(MockMvcResultMatchers.jsonPath("$.refreshToken").value("new-refresh-token"))
    }
}
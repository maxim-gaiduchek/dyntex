package cz.cvut.fit.sp1.api.controller

import cz.cvut.fit.sp1.api.data.UserAccountTestData
import cz.cvut.fit.sp1.api.data.repository.UserAccountRepository
import cz.cvut.fit.sp1.api.data.service.interfaces.UserAccountService
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
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
    lateinit var userAccountRepository: UserAccountRepository

    @MockBean
    lateinit var javaMailSender: JavaMailSender

    @BeforeEach
    @AfterEach
    fun afterEach() {
        userAccountRepository.deleteAll()
    }

    @Test
    fun `test getAll without parameters`() {
        val user0 = UserAccountTestData.defaultUser0()
        val user01 = UserAccountTestData.defaultUser01()
        val user10 = UserAccountTestData.defaultUser10()
        userAccountService.save(user0)
        userAccountService.save(user01)
        userAccountService.save(user10)
        mockMvc.perform(
            MockMvcRequestBuilders.get("/users"),
        )
            .andExpect(
                status().isOk(),
            )
            .andExpect(MockMvcResultMatchers.jsonPath("$.currentPage").value(1))
            .andExpect(MockMvcResultMatchers.jsonPath("$.totalPages").value(1))
            .andExpect(MockMvcResultMatchers.jsonPath("$.totalMatches").value(3))
            .andExpect(MockMvcResultMatchers.jsonPath("$.userAccounts").isArray())
            .andExpect(MockMvcResultMatchers.jsonPath("$.userAccounts.length()").value(3))
            .andExpect(MockMvcResultMatchers.jsonPath("$.userAccounts[0].name").value(user10.name))
            .andExpect(MockMvcResultMatchers.jsonPath("$.userAccounts[1].name").value(user01.name))
            .andExpect(MockMvcResultMatchers.jsonPath("$.userAccounts[2].name").value(user0.name))
    }

    @Test
    fun `test getAll with sort by name with ascending direction`() {
        val user0 = UserAccountTestData.defaultUser0()
        val user01 = UserAccountTestData.defaultUser01()
        val user10 = UserAccountTestData.defaultUser10()
        userAccountService.save(user0)
        userAccountService.save(user01)
        userAccountService.save(user10)
        val sortBy = "name"
        val sortDirection = "asc"
        mockMvc.perform(
            MockMvcRequestBuilders.get(
                "/users?" +
                        "sortBy=$sortBy&" +
                        "sortDirection=$sortDirection"
            ),
        )
            .andExpect(
                status().isOk(),
            )
            .andExpect(MockMvcResultMatchers.jsonPath("$.currentPage").value(1))
            .andExpect(MockMvcResultMatchers.jsonPath("$.totalPages").value(1))
            .andExpect(MockMvcResultMatchers.jsonPath("$.totalMatches").value(3))
            .andExpect(MockMvcResultMatchers.jsonPath("$.userAccounts").isArray())
            .andExpect(MockMvcResultMatchers.jsonPath("$.userAccounts.length()").value(3))
            .andExpect(MockMvcResultMatchers.jsonPath("$.userAccounts[0].name").value(user0.name))
            .andExpect(MockMvcResultMatchers.jsonPath("$.userAccounts[1].name").value(user01.name))
            .andExpect(MockMvcResultMatchers.jsonPath("$.userAccounts[2].name").value(user10.name))
    }

    @Test
    fun `test getAll with sort by name with descending direction`() {
        val user0 = UserAccountTestData.defaultUser0()
        val user01 = UserAccountTestData.defaultUser01()
        val user10 = UserAccountTestData.defaultUser10()
        userAccountService.save(user0)
        userAccountService.save(user01)
        userAccountService.save(user10)
        val sortBy = "name"
        val sortDirection = "desc"
        mockMvc.perform(
            MockMvcRequestBuilders.get(
                "/users?" +
                        "sortBy=$sortBy&" +
                        "sortDirection=$sortDirection"
            ),
        )
            .andExpect(
                status().isOk(),
            )
            .andExpect(MockMvcResultMatchers.jsonPath("$.currentPage").value(1))
            .andExpect(MockMvcResultMatchers.jsonPath("$.totalPages").value(1))
            .andExpect(MockMvcResultMatchers.jsonPath("$.totalMatches").value(3))
            .andExpect(MockMvcResultMatchers.jsonPath("$.userAccounts").isArray())
            .andExpect(MockMvcResultMatchers.jsonPath("$.userAccounts.length()").value(3))
            .andExpect(MockMvcResultMatchers.jsonPath("$.userAccounts[0].name").value(user10.name))
            .andExpect(MockMvcResultMatchers.jsonPath("$.userAccounts[1].name").value(user01.name))
            .andExpect(MockMvcResultMatchers.jsonPath("$.userAccounts[2].name").value(user0.name))
    }

    @Test
    fun `test getAll with sort by name with ascending direction page 1 pageSize 1`() {
        val user0 = UserAccountTestData.defaultUser0()
        val user01 = UserAccountTestData.defaultUser01()
        val user10 = UserAccountTestData.defaultUser10()
        userAccountService.save(user0)
        userAccountService.save(user01)
        userAccountService.save(user10)
        val sortBy = "name"
        val sortDirection = "asc"
        val page = 1
        val pageSize = 1
        mockMvc.perform(
            MockMvcRequestBuilders.get(
                "/users?" +
                        "sortBy=$sortBy&" +
                        "sortDirection=$sortDirection&" +
                        "page=$page&" +
                        "pageSize=$pageSize"
            ),
        )
            .andExpect(
                status().isOk(),
            )
            .andExpect(MockMvcResultMatchers.jsonPath("$.currentPage").value(1))
            .andExpect(MockMvcResultMatchers.jsonPath("$.totalPages").value(3))
            .andExpect(MockMvcResultMatchers.jsonPath("$.totalMatches").value(3))
            .andExpect(MockMvcResultMatchers.jsonPath("$.userAccounts").isArray())
            .andExpect(MockMvcResultMatchers.jsonPath("$.userAccounts.length()").value(1))
            .andExpect(MockMvcResultMatchers.jsonPath("$.userAccounts[0].name").value(user0.name))
    }

    @Test
    fun `test getAll with sort by name with ascending direction page 2 pageSize 1`() {
        val user0 = UserAccountTestData.defaultUser0()
        val user01 = UserAccountTestData.defaultUser01()
        val user10 = UserAccountTestData.defaultUser10()
        userAccountService.save(user0)
        userAccountService.save(user01)
        userAccountService.save(user10)
        val sortBy = "name"
        val sortDirection = "asc"
        val page = 2
        val pageSize = 1
        mockMvc.perform(
            MockMvcRequestBuilders.get(
                "/users?" +
                        "sortBy=$sortBy&" +
                        "sortDirection=$sortDirection&" +
                        "page=$page&" +
                        "pageSize=$pageSize"
            ),
        )
            .andExpect(
                status().isOk(),
            )
            .andExpect(MockMvcResultMatchers.jsonPath("$.currentPage").value(2))
            .andExpect(MockMvcResultMatchers.jsonPath("$.totalPages").value(3))
            .andExpect(MockMvcResultMatchers.jsonPath("$.totalMatches").value(3))
            .andExpect(MockMvcResultMatchers.jsonPath("$.userAccounts").isArray())
            .andExpect(MockMvcResultMatchers.jsonPath("$.userAccounts.length()").value(1))
            .andExpect(MockMvcResultMatchers.jsonPath("$.userAccounts[0].name").value(user01.name))
    }

    @Test
    fun `test getAll with sort by name with ascending direction page 4 pageSize 1`() {
        val user0 = UserAccountTestData.defaultUser0()
        val user01 = UserAccountTestData.defaultUser01()
        val user10 = UserAccountTestData.defaultUser10()
        userAccountService.save(user0)
        userAccountService.save(user01)
        userAccountService.save(user10)
        val sortBy = "name"
        val sortDirection = "asc"
        val page = 4
        val pageSize = 1
        mockMvc.perform(
            MockMvcRequestBuilders.get(
                "/users?" +
                        "sortBy=$sortBy&" +
                        "sortDirection=$sortDirection&" +
                        "page=$page&" +
                        "pageSize=$pageSize"
            ),
        )
            .andExpect(
                status().isOk(),
            )
            .andExpect(MockMvcResultMatchers.jsonPath("$.currentPage").value(4))
            .andExpect(MockMvcResultMatchers.jsonPath("$.totalPages").value(3))
            .andExpect(MockMvcResultMatchers.jsonPath("$.totalMatches").value(3))
            .andExpect(MockMvcResultMatchers.jsonPath("$.userAccounts").isArray())
            .andExpect(MockMvcResultMatchers.jsonPath("$.userAccounts").isEmpty)
    }

    @Test
    fun `test getAll with filter name sort by name with ascending direction`() {
        val user0 = UserAccountTestData.defaultUser0()
        val user01 = UserAccountTestData.defaultUser01()
        val user10 = UserAccountTestData.defaultUser10()
        userAccountService.save(user0)
        userAccountService.save(user01)
        userAccountService.save(user10)
        val name = "1"
        val sortBy = "name"
        val sortDirection = "asc"
        mockMvc.perform(
            MockMvcRequestBuilders.get(
                "/users?" +
                        "name=$name&" +
                        "sortBy=$sortBy&" +
                        "sortDirection=$sortDirection"
            ),
        )
            .andExpect(
                status().isOk(),
            )
            .andExpect(MockMvcResultMatchers.jsonPath("$.currentPage").value(1))
            .andExpect(MockMvcResultMatchers.jsonPath("$.totalPages").value(1))
            .andExpect(MockMvcResultMatchers.jsonPath("$.totalMatches").value(2))
            .andExpect(MockMvcResultMatchers.jsonPath("$.userAccounts").isArray())
            .andExpect(MockMvcResultMatchers.jsonPath("$.userAccounts.length()").value(2))
            .andExpect(MockMvcResultMatchers.jsonPath("$.userAccounts[0].name").value(user01.name))
            .andExpect(MockMvcResultMatchers.jsonPath("$.userAccounts[1].name").value(user10.name))
    }

    @Test
    fun `test getAll with filter id sort by name with ascending direction`() {
        var user0 = UserAccountTestData.defaultUser0()
        val user01 = UserAccountTestData.defaultUser01()
        val user10 = UserAccountTestData.defaultUser10()
        user0 = userAccountService.save(user0)
        userAccountService.save(user01)
        userAccountService.save(user10)
        val id = user0.id
        val sortBy = "name"
        val sortDirection = "asc"
        mockMvc.perform(
            MockMvcRequestBuilders.get(
                "/users?" +
                        "ids=$id&" +
                        "sortBy=$sortBy&" +
                        "sortDirection=$sortDirection"
            ),
        )
            .andExpect(
                status().isOk(),
            )
            .andExpect(MockMvcResultMatchers.jsonPath("$.currentPage").value(1))
            .andExpect(MockMvcResultMatchers.jsonPath("$.totalPages").value(1))
            .andExpect(MockMvcResultMatchers.jsonPath("$.totalMatches").value(1))
            .andExpect(MockMvcResultMatchers.jsonPath("$.userAccounts").isArray())
            .andExpect(MockMvcResultMatchers.jsonPath("$.userAccounts.length()").value(1))
            .andExpect(MockMvcResultMatchers.jsonPath("$.userAccounts[0].name").value(user0.name))
    }

    @Test
    fun `test getAll with filter ids sort by name with ascending direction`() {
        var user0 = UserAccountTestData.defaultUser0()
        val user01 = UserAccountTestData.defaultUser01()
        var user10 = UserAccountTestData.defaultUser10()
        user0 = userAccountService.save(user0)
        userAccountService.save(user01)
        user10 = userAccountService.save(user10)
        val id0 = user0.id
        val id10 = user10.id
        val sortBy = "name"
        val sortDirection = "asc"
        mockMvc.perform(
            MockMvcRequestBuilders.get(
                "/users?" +
                        "ids=$id0,$id10&" +
                        "sortBy=$sortBy&" +
                        "sortDirection=$sortDirection"
            ),
        )
            .andExpect(
                status().isOk(),
            )
            .andExpect(MockMvcResultMatchers.jsonPath("$.currentPage").value(1))
            .andExpect(MockMvcResultMatchers.jsonPath("$.totalPages").value(1))
            .andExpect(MockMvcResultMatchers.jsonPath("$.totalMatches").value(2))
            .andExpect(MockMvcResultMatchers.jsonPath("$.userAccounts").isArray())
            .andExpect(MockMvcResultMatchers.jsonPath("$.userAccounts.length()").value(2))
            .andExpect(MockMvcResultMatchers.jsonPath("$.userAccounts[0].name").value(user0.name))
            .andExpect(MockMvcResultMatchers.jsonPath("$.userAccounts[1].name").value(user10.name))
    }

    @Test
    fun `test getAll with filter ids and name sort by name with ascending direction`() {
        val user0 = UserAccountTestData.defaultUser0()
        val user01 = UserAccountTestData.defaultUser01()
        val user10 = UserAccountTestData.defaultUser10()
        userAccountService.save(user0)
        userAccountService.save(user01)
        userAccountService.save(user10)
        val id0 = user0.id
        val id10 = user10.id
        val name = user0.name
        val sortBy = "name"
        val sortDirection = "asc"
        mockMvc.perform(
            MockMvcRequestBuilders.get(
                "/users?" +
                        "ids=$id0,$id10&" +
                        "name=$name&" +
                        "sortBy=$sortBy&" +
                        "sortDirection=$sortDirection"
            ),
        )
            .andExpect(
                status().isOk(),
            )
            .andExpect(MockMvcResultMatchers.jsonPath("$.currentPage").value(1))
            .andExpect(MockMvcResultMatchers.jsonPath("$.totalPages").value(1))
            .andExpect(MockMvcResultMatchers.jsonPath("$.totalMatches").value(1))
            .andExpect(MockMvcResultMatchers.jsonPath("$.userAccounts").isArray())
            .andExpect(MockMvcResultMatchers.jsonPath("$.userAccounts.length()").value(1))
            .andExpect(MockMvcResultMatchers.jsonPath("$.userAccounts[0].name").value(user0.name))
    }
}

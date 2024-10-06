package cz.cvut.fit.sp1.api.controller

import cz.cvut.fit.sp1.api.data.TagTestData
import cz.cvut.fit.sp1.api.data.repository.TagRepository
import cz.cvut.fit.sp1.api.data.service.interfaces.TagService
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
class TagControllerTest {
    @Autowired
    lateinit var mockMvc: MockMvc

    @Autowired
    lateinit var tagService: TagService

    @Autowired
    lateinit var tagRepository: TagRepository

    @MockBean
    lateinit var javaMailSender: JavaMailSender

    @BeforeEach
    @AfterEach
    fun afterEach() {
        tagRepository.deleteAll()
    }

    @Test
    fun `test getAll without parameters`() {
        val tag0 = TagTestData.defaultTag0()
        val tag01 = TagTestData.defaultTag01()
        val tag10 = TagTestData.defaultTag10()
        tagService.save(tag0)
        tagService.save(tag01)
        tagService.save(tag10)
        mockMvc.perform(
            MockMvcRequestBuilders.get("/tags"),
        )
            .andExpect(
                status().isOk(),
            )
            .andExpect(MockMvcResultMatchers.jsonPath("$.currentPage").value(1))
            .andExpect(MockMvcResultMatchers.jsonPath("$.totalPages").value(1))
            .andExpect(MockMvcResultMatchers.jsonPath("$.totalMatches").value(3))
            .andExpect(MockMvcResultMatchers.jsonPath("$.tags").isArray())
            .andExpect(MockMvcResultMatchers.jsonPath("$.tags.length()").value(3))
            .andExpect(MockMvcResultMatchers.jsonPath("$.tags[0].name").value(tag10.name))
            .andExpect(MockMvcResultMatchers.jsonPath("$.tags[1].name").value(tag01.name))
            .andExpect(MockMvcResultMatchers.jsonPath("$.tags[2].name").value(tag0.name))
    }

    @Test
    fun `test getAll with sort by name with ascending direction`() {
        val tag0 = TagTestData.defaultTag0()
        val tag01 = TagTestData.defaultTag01()
        val tag10 = TagTestData.defaultTag10()
        tagService.save(tag0)
        tagService.save(tag01)
        tagService.save(tag10)
        val sortBy = "name"
        val sortDirection = "asc"
        mockMvc.perform(
            MockMvcRequestBuilders.get(
                "/tags?" +
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
            .andExpect(MockMvcResultMatchers.jsonPath("$.tags").isArray())
            .andExpect(MockMvcResultMatchers.jsonPath("$.tags.length()").value(3))
            .andExpect(MockMvcResultMatchers.jsonPath("$.tags[0].name").value(tag0.name))
            .andExpect(MockMvcResultMatchers.jsonPath("$.tags[1].name").value(tag01.name))
            .andExpect(MockMvcResultMatchers.jsonPath("$.tags[2].name").value(tag10.name))
    }

    @Test
    fun `test getAll with sort by name with descending direction`() {
        val tag0 = TagTestData.defaultTag0()
        val tag01 = TagTestData.defaultTag01()
        val tag10 = TagTestData.defaultTag10()
        tagService.save(tag0)
        tagService.save(tag01)
        tagService.save(tag10)
        val sortBy = "name"
        val sortDirection = "desc"
        mockMvc.perform(
            MockMvcRequestBuilders.get(
                "/tags?" +
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
            .andExpect(MockMvcResultMatchers.jsonPath("$.tags").isArray())
            .andExpect(MockMvcResultMatchers.jsonPath("$.tags.length()").value(3))
            .andExpect(MockMvcResultMatchers.jsonPath("$.tags[0].name").value(tag10.name))
            .andExpect(MockMvcResultMatchers.jsonPath("$.tags[1].name").value(tag01.name))
            .andExpect(MockMvcResultMatchers.jsonPath("$.tags[2].name").value(tag0.name))
    }

    @Test
    fun `test getAll with sort by name with ascending direction page 1 pageSize 1`() {
        val tag0 = TagTestData.defaultTag0()
        val tag01 = TagTestData.defaultTag01()
        val tag10 = TagTestData.defaultTag10()
        tagService.save(tag0)
        tagService.save(tag01)
        tagService.save(tag10)
        val sortBy = "name"
        val sortDirection = "asc"
        val page = 1
        val pageSize = 1
        mockMvc.perform(
            MockMvcRequestBuilders.get(
                "/tags?" +
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
            .andExpect(MockMvcResultMatchers.jsonPath("$.tags").isArray())
            .andExpect(MockMvcResultMatchers.jsonPath("$.tags.length()").value(1))
            .andExpect(MockMvcResultMatchers.jsonPath("$.tags[0].name").value(tag0.name))
    }

    @Test
    fun `test getAll with sort by name with ascending direction page 2 pageSize 1`() {
        val tag0 = TagTestData.defaultTag0()
        val tag01 = TagTestData.defaultTag01()
        val tag10 = TagTestData.defaultTag10()
        tagService.save(tag0)
        tagService.save(tag01)
        tagService.save(tag10)
        val sortBy = "name"
        val sortDirection = "asc"
        val page = 2
        val pageSize = 1
        mockMvc.perform(
            MockMvcRequestBuilders.get(
                "/tags?" +
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
            .andExpect(MockMvcResultMatchers.jsonPath("$.tags").isArray())
            .andExpect(MockMvcResultMatchers.jsonPath("$.tags.length()").value(1))
            .andExpect(MockMvcResultMatchers.jsonPath("$.tags[0].name").value(tag01.name))
    }

    @Test
    fun `test getAll with sort by name with ascending direction page 4 pageSize 1`() {
        val tag0 = TagTestData.defaultTag0()
        val tag01 = TagTestData.defaultTag01()
        val tag10 = TagTestData.defaultTag10()
        tagService.save(tag0)
        tagService.save(tag01)
        tagService.save(tag10)
        val sortBy = "name"
        val sortDirection = "asc"
        val page = 4
        val pageSize = 1
        mockMvc.perform(
            MockMvcRequestBuilders.get(
                "/tags?" +
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
            .andExpect(MockMvcResultMatchers.jsonPath("$.tags").isArray())
            .andExpect(MockMvcResultMatchers.jsonPath("$.tags").isEmpty)
    }

    @Test
    fun `test getAll with filter name sort by name with ascending direction`() {
        val tag0 = TagTestData.defaultTag0()
        val tag01 = TagTestData.defaultTag01()
        val tag10 = TagTestData.defaultTag10()
        tagService.save(tag0)
        tagService.save(tag01)
        tagService.save(tag10)
        val name = "1"
        val sortBy = "name"
        val sortDirection = "asc"
        mockMvc.perform(
            MockMvcRequestBuilders.get(
                "/tags?" +
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
            .andExpect(MockMvcResultMatchers.jsonPath("$.tags").isArray())
            .andExpect(MockMvcResultMatchers.jsonPath("$.tags.length()").value(2))
            .andExpect(MockMvcResultMatchers.jsonPath("$.tags[0].name").value(tag01.name))
            .andExpect(MockMvcResultMatchers.jsonPath("$.tags[1].name").value(tag10.name))
    }

    @Test
    fun `test getAll with filter id sort by name with ascending direction`() {
        var tag0 = TagTestData.defaultTag0()
        val tag01 = TagTestData.defaultTag01()
        val tag10 = TagTestData.defaultTag10()
        tag0 = tagService.save(tag0)
        tagService.save(tag01)
        tagService.save(tag10)
        val id = tag0.id
        val sortBy = "name"
        val sortDirection = "asc"
        mockMvc.perform(
            MockMvcRequestBuilders.get(
                "/tags?" +
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
            .andExpect(MockMvcResultMatchers.jsonPath("$.tags").isArray())
            .andExpect(MockMvcResultMatchers.jsonPath("$.tags.length()").value(1))
            .andExpect(MockMvcResultMatchers.jsonPath("$.tags[0].name").value(tag0.name))
    }

    @Test
    fun `test getAll with filter ids sort by name with ascending direction`() {
        var tag0 = TagTestData.defaultTag0()
        val tag01 = TagTestData.defaultTag01()
        var tag10 = TagTestData.defaultTag10()
        tag0 = tagService.save(tag0)
        tagService.save(tag01)
        tag10 = tagService.save(tag10)
        val id0 = tag0.id
        val id10 = tag10.id
        val sortBy = "name"
        val sortDirection = "asc"
        mockMvc.perform(
            MockMvcRequestBuilders.get(
                "/tags?" +
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
            .andExpect(MockMvcResultMatchers.jsonPath("$.tags").isArray())
            .andExpect(MockMvcResultMatchers.jsonPath("$.tags.length()").value(2))
            .andExpect(MockMvcResultMatchers.jsonPath("$.tags[0].name").value(tag0.name))
            .andExpect(MockMvcResultMatchers.jsonPath("$.tags[1].name").value(tag10.name))
    }

    @Test
    fun `test getAll with filter ids and name sort by name with ascending direction`() {
        val tag0 = TagTestData.defaultTag0()
        val tag01 = TagTestData.defaultTag01()
        val tag10 = TagTestData.defaultTag10()
        tagService.save(tag0)
        tagService.save(tag01)
        tagService.save(tag10)
        val id0 = tag0.id
        val id10 = tag10.id
        val name = tag0.name
        val sortBy = "name"
        val sortDirection = "asc"
        mockMvc.perform(
            MockMvcRequestBuilders.get(
                "/tags?" +
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
            .andExpect(MockMvcResultMatchers.jsonPath("$.tags").isArray())
            .andExpect(MockMvcResultMatchers.jsonPath("$.tags.length()").value(1))
            .andExpect(MockMvcResultMatchers.jsonPath("$.tags[0].name").value(tag0.name))
    }
}

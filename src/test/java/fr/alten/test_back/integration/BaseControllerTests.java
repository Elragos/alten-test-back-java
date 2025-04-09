package fr.alten.test_back.integration;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import fr.alten.test_back.TestContainersConfiguration;
import fr.alten.test_back.TestData;
import fr.alten.test_back.dto.user.CreateUserDto;
import fr.alten.test_back.dto.user.LoginUserDto;
import fr.alten.test_back.helper.AppRoutes;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.Map;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

/**
 * Base class to implements controller tests.
 *
 * @author amarechal
 */
@SpringBootTest
@Import(TestContainersConfiguration.class)
@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class BaseControllerTests {

    /**
     * Used mock to launch HTTP test queries.
     */
    @Autowired
    protected MockMvc mockMvc;

    /**
     * Used object mapper to write JSON data.
     */
    @Autowired
    protected ObjectMapper mapper;

    /**
     * Used test data.
     */
    @Autowired
    protected TestData data;

    /**
     * Load test data before each test.
     *
     * @throws JsonProcessingException If JSON is malformed.
     */
    @BeforeEach
    public void setUp() throws JsonProcessingException {
        // Load JSON data
        this.data.loadData();
    }

    /**
     * Get JWT token for authentication.
     *
     * @param user Desired user.
     * @return JWT token
     * @throws Exception If login went wrong.
     */
    public String getJwtToken(CreateUserDto user) throws Exception {
        LoginUserDto loginData = new LoginUserDto(user.email(), user.password());
        // Perform login request and get response
        MvcResult loginResponse = this.mockMvc.perform(
            post(AppRoutes.LOGIN).contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(loginData))
        ).andReturn();
        // Parse response
        Map<String, Object> loginResponseData = this.mapper.readValue(
            loginResponse.getResponse().getContentAsString(),
            new TypeReference<>() {}
        );

        return loginResponseData.get("value").toString();
    }

}

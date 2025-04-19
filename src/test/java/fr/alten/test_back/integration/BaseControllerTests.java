package fr.alten.test_back.integration;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import fr.alten.test_back.config.TestcontainersConfig;
import fr.alten.test_back.dto.user.CreateUserDto;
import fr.alten.test_back.dto.user.LoginUserDto;
import fr.alten.test_back.helper.AppRoutes;
import fr.alten.test_back.helper.JsonDataParser;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ResourceUtils;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Map;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

/**
 * Base class to implements controller tests.
 *
 * @author amarechal
 */
@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@Rollback
@ActiveProfiles("test")
@Import(TestcontainersConfig.class)
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
    protected JsonDataParser testData;

    /**
     * Load test data before each test.
     *
     * @throws RuntimeException If JSON is malformed.
     */
    @BeforeEach
    public void setUp() throws RuntimeException {
        if (this.testData == null){
            // Load JSON data content
            String jsonContent;
            try (InputStream input = ResourceUtils.getURL("classpath:testData.json").openStream()) {
                jsonContent = new String(input.readAllBytes(), StandardCharsets.UTF_8);
            } catch (IOException e) {
                throw new RuntimeException("Impossible de lire le fichier de test testData.json", e);
            }

            // Parse data content
            this.testData = new JsonDataParser(
                    new ObjectMapper(),
                    jsonContent
            );
        }
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

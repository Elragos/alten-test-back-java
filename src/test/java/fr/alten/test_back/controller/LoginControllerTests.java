package fr.alten.test_back.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import fr.alten.test_back.TestContainersConfiguration;
import fr.alten.test_back.TestData;
import fr.alten.test_back.dto.CreateUserDto;
import fr.alten.test_back.dto.LoginUserDto;
import fr.alten.test_back.dto.RegisterUserDto;
import fr.alten.test_back.entity.RoleEnum;
import fr.alten.test_back.entity.User;
import fr.alten.test_back.helper.AppRoutes;
import fr.alten.test_back.repository.UserRepository;
import java.util.Map;
import org.assertj.core.api.Assertions;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.not;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * LoginController tests.
 *
 * @author amarechal
 */
@SpringBootTest
@Import(TestContainersConfiguration.class)
@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class LoginControllerTests {

    /**
     * Used mock to launch HTTP test queries.
     */
    @Autowired
    private MockMvc mockMvc;

    /**
     * Used userRepository.
     */
    @Autowired
    private UserRepository userRepository;

    /**
     * Used object mapper to write JSON data.
     */
    @Autowired
    private ObjectMapper mapper;

    /**
     * Used test data.
     */
    @Autowired
    private TestData data;

    /**
     * Load test data before testing.
     *
     * @throws JsonProcessingException If JSON is malformed.
     */
    @BeforeEach
    public void setUp() throws JsonProcessingException {
        // Load JSON data
        this.data.loadData();
    }

    /**
     * Test admin login.
     *
     * @throws Exception If test went wrong.
     */
    @Test
    @Order(1)
    public void adminLoginShouldSucceed() throws Exception {
        // Get admin user
        CreateUserDto admin = this.data.getUsers().get(0);
        // Set POST data
        LoginUserDto loginData = new LoginUserDto()
                .setEmail(admin.getEmail())
                .setPassword(admin.getPassword());
        // Perform action
        this.mockMvc.perform(
                post(AppRoutes.LOGIN).contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(loginData))
        ).
            // Print result
            andDo(print())
            // Test HTTP response is OK
            .andExpect(status().isOk())
            // Test token is in response
            .andExpect(jsonPath("$.token").isNotEmpty())
            // Test expiresIn in response
            .andExpect(jsonPath("$.expiresIn").isNotEmpty());
    }
    
    /**
     * Test admin login is logged as admin.
     *
     * @throws Exception If test went wrong.
     */
    @Test
    @Order(2)
    public void adminLoggingAsAdmin() throws Exception {
        // Get admin user
        CreateUserDto user = this.data.getUsers().get(0);
        // Set POST data
        LoginUserDto loginData = new LoginUserDto()
            .setEmail(user.getEmail())
            .setPassword(user.getPassword());
        // Peform login request and get response
        MvcResult loginResponse = this.mockMvc.perform(
            post(AppRoutes.LOGIN).contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(loginData))
        ).andReturn();
        // Parse response
        Map loginResponseData = this.mapper.readValue(
            loginResponse.getResponse().getContentAsString(), 
            new TypeReference<Map<String, Object>>() {}
        );
        // Get user info
        this.mockMvc.perform(
            get(AppRoutes.USER_INFO)
                .header("Authorization", "Bearer " + loginResponseData.get("token"))
        )
            // Print result
            .andDo(print())
            // Test HTTP response is OK
            .andExpect(status().isOk())
            // Test admin has role admin
            .andExpect(
                jsonPath("$.principal.roles[*].role",
                    hasItem(RoleEnum.ROLE_ADMIN.name())
                )
            );
      
    }
    
    /**
     * Test user login is logged as user.
     *
     * @throws Exception If test went wrong.
     */
    @Test
    @Order(3)
    public void userLoggingAsUser() throws Exception {
        // Get admin user
        CreateUserDto user = this.data.getUsers().get(1);
        // Set POST data
        LoginUserDto loginData = new LoginUserDto()
            .setEmail(user.getEmail())
            .setPassword(user.getPassword());
        // Peform login request and get response
        MvcResult loginResponse = this.mockMvc.perform(
            post(AppRoutes.LOGIN).contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(loginData))
        ).andReturn();
        // Parse response
        Map loginResponseData = this.mapper.readValue(
            loginResponse.getResponse().getContentAsString(), 
            new TypeReference<Map<String, Object>>() {}
        );
        // Get user info
        this.mockMvc.perform(
            get(AppRoutes.USER_INFO)
                .header("Authorization", "Bearer " + loginResponseData.get("token"))
        )
            // Print result
            .andDo(print())
            // Test HTTP response is OK
            .andExpect(status().isOk())
            // Test user has role user
            .andExpect(
                jsonPath("$.principal.roles[*].role",
                    hasItem(RoleEnum.ROLE_USER.name())
                )
            )
            // Test user is not admin
            .andExpect(
                jsonPath("$.principal.roles[*].role",
                    not(hasItem(RoleEnum.ROLE_ADMIN.name()))
                )
            );
    }
    
    /**
     * Test logging with bad credentials fails.
     *
     * @throws Exception If test went wrong.
     */
    @Test
    @Order(4)
    public void badCredentialsFails() throws Exception {
         // Set POST data
        LoginUserDto loginData = new LoginUserDto()
                .setEmail("shouldNotExists")
                .setPassword("");
        // Perform action
        this.mockMvc.perform(
                post(AppRoutes.LOGIN).contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(loginData))
        ).
            // Print result
            andDo(print())
            // Test HTTP response is UnAuthorized
            .andExpect(status().isUnauthorized());
    }
    
    /**
     * Test user creation is successful.
     *
     * @throws Exception If test went wrong.
     */
    @Test
    @Order(5)
    public void userCreationIsSuccess() throws Exception {
        RegisterUserDto dto = new RegisterUserDto()
            .setEmail("testCreation@test.com")
            .setFirstname("test")
            .setUsername("test")
            .setPassword("123456");
        
        this.mockMvc.perform(
            post(AppRoutes.CREATE_ACCOUNT).contentType(MediaType.APPLICATION_JSON)
                .content(this.mapper.writeValueAsString(dto))
        ).
            // Print result
            andDo(print())
            // Test HTTP response is OK
            .andExpect(status().isOk())
            // Test registered email match dto email
            .andExpect(jsonPath("$.email").value(dto.getEmail()))
            // Test registered firstname match dto firstname
            .andExpect(jsonPath("$.firstname").value(dto.getFirstname()))
            // Test registered username match dto username
            .andExpect(jsonPath("$.username").value(dto.getUsername()))
            ;
        
        // Check that registered user is only user
        User createdUser = this.userRepository.findByEmail(dto.getEmail())
            .orElseThrow();
        
        // Assert that created user has role user
        Assertions.assertThat(
            createdUser.getRoles().stream().anyMatch(
                role -> role.getRole().equals(RoleEnum.ROLE_USER)
            )
        );
        // Assert that created user is not admin
        Assertions.assertThat(
            createdUser.getRoles().stream().noneMatch(
                role -> role.getRole().equals(RoleEnum.ROLE_ADMIN)
            )
        );
    }
    
    /**
     * Test if registering with existing email fails.
     *
     * @throws Exception If test went wrong.
     */
    @Test
    @Order(6)
    public void cannotReuseSameEmail() throws Exception {        
        RegisterUserDto dto = this.data.getUsers().get(0);
        
        this.mockMvc.perform(
            post(AppRoutes.CREATE_ACCOUNT).contentType(MediaType.APPLICATION_JSON)
                .content(this.mapper.writeValueAsString(dto))
        ).
            // Print result
            andDo(print())
            // Test HTTP response is Bad request
            .andExpect(status().isBadRequest());
    }
}

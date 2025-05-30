package fr.alten.test_back.integration;

import fr.alten.test_back.dto.user.CreateUserDto;
import fr.alten.test_back.dto.user.LoginUserDto;
import fr.alten.test_back.dto.user.RegisterUserDto;
import fr.alten.test_back.entity.RoleEnum;
import fr.alten.test_back.helper.AppRoutes;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.not;
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
public class UserControllerTests extends BaseControllerTests {

    /**
     * Test admin login.
     *
     * @throws Exception If test went wrong.
     */
    @Test
    public void adminLoginShouldSucceed() throws Exception {
        // Get admin user
        CreateUserDto admin = this.testData.getUsers().getFirst();
        // Set POST data
        LoginUserDto loginData = new LoginUserDto(admin.email(), admin.password());
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
            .andExpect(jsonPath("$.value").isNotEmpty())
            // Test expiresAt in response
            .andExpect(jsonPath("$.expirationDate").isNotEmpty());
    }
    
    /**
     * Test admin login is logged as admin.
     *
     * @throws Exception If test went wrong.
     */
    @Test
    public void adminLoggingAsAdmin() throws Exception {
        // Get admin user
        CreateUserDto user = this.testData.getUsers().getFirst();
        // Get token
        String token = this.getJwtToken(user);
        // Get user info
        this.mockMvc.perform(
            get(AppRoutes.USER_INFO)
                .header("Authorization", "Bearer " + token)
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
    public void userLoggingAsUser() throws Exception {
        // Get user
        CreateUserDto user = this.testData.getUsers().get(1);
      
        // Get token
        String token = this.getJwtToken(user);
        
        // Get user info
        this.mockMvc.perform(
            get(AppRoutes.USER_INFO)
                .header("Authorization", "Bearer " + token)
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
    public void badCredentialsFails() throws Exception {
         // Set POST data
        LoginUserDto loginData = new LoginUserDto("", "");
        // Perform action
        this.mockMvc.perform(post(AppRoutes.LOGIN)
            .contentType(MediaType.APPLICATION_JSON)
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
    public void userCreationIsSuccess() throws Exception {
        RegisterUserDto dto = new RegisterUserDto(
            "testCreation@test.com",
            "test",
            "test",
            "123456"
        );
        
        this.mockMvc.perform(
            post(AppRoutes.CREATE_ACCOUNT).contentType(MediaType.APPLICATION_JSON)
                .content(this.mapper.writeValueAsString(dto))
        ).
            // Print result
            andDo(print())
            // Test HTTP response is OK
            .andExpect(status().isOk())
            // Test registered email match dto email
            .andExpect(jsonPath("$.email").value(dto.email()))
            // Test registered firstname match dto firstname
            .andExpect(jsonPath("$.firstname").value(dto.firstname()))
            // Test registered username match dto username
            .andExpect(jsonPath("$.username").value(dto.username()))
            ;
    }
    
    /**
     * Test if registering with existing email fails.
     *
     * @throws Exception If test went wrong.
     */
    @Test
    public void cannotReuseSameEmail() throws Exception {        
        CreateUserDto dto = this.testData.getUsers().getFirst();
        
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

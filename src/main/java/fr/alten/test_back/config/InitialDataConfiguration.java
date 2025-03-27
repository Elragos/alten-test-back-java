package fr.alten.test_back.config;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import fr.alten.test_back.dto.CreateUserDto;
import fr.alten.test_back.dto.ProductDto;
import fr.alten.test_back.entity.Role;
import fr.alten.test_back.entity.RoleEnum;
import fr.alten.test_back.entity.Product;
import fr.alten.test_back.entity.User;
import fr.alten.test_back.helper.InitialDataParser;
import fr.alten.test_back.repository.ProductRepository;
import fr.alten.test_back.repository.UserRepository;
import jakarta.annotation.PostConstruct;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.security.crypto.password.PasswordEncoder;
import fr.alten.test_back.repository.RoleRepository;

/**
 * Initial data configuration. This ensures that all authorities are registered
 * in database and default admin account exists.
 *
 * @author amarechal
 */
@Configuration
@PropertySource(
    value = "classpath:initialData.json",
    factory = JsonPropertySourceFactory.class
)
public class InitialDataConfiguration {

    @Autowired
    private Environment env;

    /**
     * Used user repository.
     */
    @Autowired
    private UserRepository userRepository;

    /**
     * Used role repository.
     */
    @Autowired
    private RoleRepository roleRepository;

    /**
     * Used password encoder.
     */
    @Autowired
    private PasswordEncoder passwordEncoder;

    /**
     * Used product repository.
     */
    @Autowired
    private ProductRepository productRepository;

    /**
     * Generate initial data after application contruction.
     *
     * @throws JsonProcessingException When JSON is malformed.
     */
    @PostConstruct
    public void postConstruct() throws JsonProcessingException {

        // For all roles available
        for (RoleEnum role : RoleEnum.values()) {
            // Create in DB
            this.generateRole(role);
        }
        // Load initial data
        loadInitialData();

        // Create admin account
        createAdminAccount();
    }

    /**
     * Create role in DB if not exists.
     *
     * @param role Desired role.
     */
    private void generateRole(RoleEnum role) {
        Optional<Role> data = this.roleRepository
            .findByRole(role);

        if (data.isEmpty()) {
            Role toCreate = new Role().setRole(role);
            this.roleRepository.save(toCreate);
        }
    }

    /**
     * Load initial data from JSON file
     * 
     * @throws JsonProcessingException When JSON is malformed.
     */
    private void loadInitialData() throws JsonProcessingException {
        // Load JSON data
        InitialDataParser parser = new InitialDataParser(
            new ObjectMapper(),
            this.env.getProperty("users", Object.class),
            this.env.getProperty("products", Object.class)
        );
     
        // For all users in initial data
        for (CreateUserDto userData : parser.getUsers()) {
            // Extract data from JSON
            User toAdd = new User()
                .setEmail(userData.getEmail())
                .setFirstname(userData.getFirstname())
                .setUsername(userData.getUsername())
                .setPassword(this.passwordEncoder.encode(
                        userData.getPassword()
                ))
                .setRoles(List.of(this.roleRepository
                    .findByRole(RoleEnum.valueOf(userData.getRole()))
                    .get()
                ));

            // Create user in DB if not exists
            this.createAccount(toAdd);
        }
        
        // For all users in initial data
        for (ProductDto productData : parser.getProducts()) {
            // Extract data from JSON
            Product toAdd = new Product(productData);

            // Create product in DB if not exists
            this.createProduct(toAdd);
        }
    }

    /**
     * Create user if not exists.
     *
     * @param toCreate User to create.
     */
    private void createAccount(User toCreate) {
        Optional<User> existingAccount = this.userRepository
            .findByEmail(toCreate.getEmail());
        if (existingAccount.isEmpty()) {
            this.userRepository.save(toCreate);
        }
    }

    /**
     * Create default admin account with pwd 123456 if not exists.
     */
    private void createAdminAccount() {
        User admin = new User()
            .setEmail("admin@admin.com")
            .setUsername("admin")
            .setFirstname("admin")
            .setPassword(this.passwordEncoder.encode("123456"))
            .setRoles(List.of(this.roleRepository
                .findByRole(RoleEnum.ROLE_ADMIN).get()
            ));
        this.createAccount(admin);
    }

    /**
     * Create product if not exists.
     *
     * @param toCreate Product to create.
     */
    private void createProduct(Product toCreate) {
        Optional<Product> existingProduct = this.productRepository
            .findByCode(toCreate.getCode());
        
        if (existingProduct.isEmpty()) {
            this.productRepository.save(toCreate);
        }
    }

}

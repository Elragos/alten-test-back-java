package fr.alten.test_back.config;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import fr.alten.test_back.dto.product.ProductDto;
import fr.alten.test_back.dto.user.CreateUserDto;
import fr.alten.test_back.entity.Product;
import fr.alten.test_back.entity.Role;
import fr.alten.test_back.entity.RoleEnum;
import fr.alten.test_back.entity.User;
import fr.alten.test_back.helper.JsonDataParser;
import fr.alten.test_back.repository.ProductRepository;
import fr.alten.test_back.repository.RoleRepository;
import fr.alten.test_back.repository.UserRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.Optional;

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

    /**
     * Used environment.
     */
    private final Environment env;

    /**
     * Used user repository.
     */
    private final UserRepository userRepository;

    /**
     * Used role repository.
     */
    private final RoleRepository roleRepository;

    /**
     * Used password encoder.
     */
    private final PasswordEncoder passwordEncoder;

    /**
     * Used product repository.
     */
    private final ProductRepository productRepository;

    public InitialDataConfiguration(
            Environment env,
            ProductRepository productRepository,
            UserRepository userRepository,
            RoleRepository roleRepository,
            PasswordEncoder passwordEncoder
    ){

        this.env = env;
        this.productRepository = productRepository;
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

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
        JsonDataParser parser = new JsonDataParser(
            new ObjectMapper(),
            this.env.getProperty("users", Object.class),
            this.env.getProperty("products", Object.class)
        );
     
        // For all users in initial data
        for (CreateUserDto userData : parser.getUsers()) {
            // Extract data from JSON
            User toAdd = new User()
                .setEmail(userData.email())
                .setFirstname(userData.firstname())
                .setUsername(userData.username())
                .setPassword(this.passwordEncoder.encode(
                        userData.password()
                ))
                .setRoles(List.of(this.roleRepository
                    .findByRole(RoleEnum.valueOf(userData.role()))
                    .orElseThrow()
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
                .findByRole(RoleEnum.ROLE_ADMIN).orElseThrow()
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

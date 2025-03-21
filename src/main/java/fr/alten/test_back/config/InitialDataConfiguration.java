package fr.alten.test_back.config;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import fr.alten.test_back.dto.CreateUserDTO;
import fr.alten.test_back.dto.ProductDto;
import fr.alten.test_back.entity.Authority;
import fr.alten.test_back.entity.AuthorityEnum;
import fr.alten.test_back.entity.Product;
import fr.alten.test_back.entity.User;
import fr.alten.test_back.repository.AuthorityRepository;
import fr.alten.test_back.repository.ProductRepository;
import fr.alten.test_back.repository.UserRepository;
import jakarta.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.security.crypto.password.PasswordEncoder;

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
     * Used authority repository.
     */
    @Autowired
    private AuthorityRepository authorityRepository;

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
     * @throws com.fasterxml.jackson.core.JsonProcessingException
     */
    @PostConstruct
    public void postConstruct() throws JsonProcessingException {

        // For all authorities available
        for (AuthorityEnum authority : AuthorityEnum.values()) {
            // Create in DB
            this.generateAuthority(authority);
        }
        // Load initial data
        loadInitialData();

        // Create admin account
        createAdminAccount();
    }

    /**
     * Create authority in DB if not exists.
     *
     * @param authority Desired authority.
     */
    private void generateAuthority(AuthorityEnum authority) {
        Optional<Authority> data = this.authorityRepository
                .findByAuthority(authority);

        if (data.isEmpty()) {
            Authority toCreate = new Authority().setAuthority(authority);
            this.authorityRepository.save(toCreate);
        }
    }

    /**
     * Load initial data from JSON file
     */
    private void loadInitialData() throws JsonProcessingException {
        // User list read from JSON file
        List<CreateUserDTO> users = new ArrayList();

        // Product list read from JSON file
        List<ProductDto> products = new ArrayList();

        // Create object mapper to load JSON data
        ObjectMapper mapper = new ObjectMapper();

        // Load user list as object
        Object usersObj = this.env.getProperty("users", Object.class);
        // If it is a list
        if (usersObj instanceof List) {
            // Parse it to JSON string
            String usersJson = mapper.writeValueAsString(usersObj);
            // Read user list from file
            users = mapper.readValue(usersJson,
                new TypeReference<List<CreateUserDTO>>() {
            });
        }

        // For all users in initial data
        for (CreateUserDTO userData : users) {
            // Extract data from JSON
            User toAdd = new User()
                .setEmail(userData.getEmail())
                .setFirstname(userData.getFirstname())
                .setUsername(userData.getUsername())
                .setPassword(this.passwordEncoder.encode(
                    userData.getPassword()
                ))
                .setAuthorities(List.of(
                    this.authorityRepository.findByAuthority(
                        AuthorityEnum.valueOf(userData.getAuthority())
                    ).get()
                ));

            // Create user in DB if not exists
            this.createAccount(toAdd);
        }

        Object productsObj = this.env.getProperty("products", Object.class);
        // If it is a list
        if (productsObj instanceof List) {
            // Parse it to JSON string
            String productsJson = mapper.writeValueAsString(productsObj);
            // Read user list from file
            products = mapper.readValue(productsJson,
                new TypeReference<List<ProductDto>>() {
            });
        }

        // For all users in initial data
        for (ProductDto productData : products) {
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
            .setAuthorities(List.of(this.authorityRepository
                .findByAuthority(AuthorityEnum.ROLE_ADMIN).get()
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

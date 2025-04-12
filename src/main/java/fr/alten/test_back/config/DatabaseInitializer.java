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
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.util.ResourceUtils;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Optional;

/**
 * Initial data configuration. This ensures that all authorities are registered
 * in database and default admin account exists.
 *
 * @author amarechal
 */
@Component
@Profile({"init-db", "test"})
public class DatabaseInitializer implements ApplicationRunner {

    /**
     * JSON data file path command parameter.
     */
    @Value("${jsonData.path:classpath:initialData.json}")
    private String jsonDataPath;

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

    /**
     * Used application context.
     */
    private final ConfigurableApplicationContext context;

    public DatabaseInitializer(
            ProductRepository productRepository,
            UserRepository userRepository,
            RoleRepository roleRepository,
            PasswordEncoder passwordEncoder,
            ConfigurableApplicationContext context
    ){

        this.productRepository = productRepository;
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
        this.context = context;
    }

    /**
     * Run database initialization.
     *
     * @throws JsonProcessingException When JSON is malformed.
     */
    @Override
    public void run(ApplicationArguments args) throws JsonProcessingException {

        // For all roles available
        for (RoleEnum role : RoleEnum.values()) {
            // Create in DB
            this.generateRole(role);
        }
        // Load initial data
        this.loadInitialData();

        // Create admin account
        this.createAdminAccount();

        // Get active profile
        String[] activeProfiles = context.getEnvironment().getActiveProfiles();
        // Determine if active profiles are standalone runs
        boolean isStandaloneRun = List.of(activeProfiles).contains("init-db");
        // If so
        if (isStandaloneRun) {
            // Shutdown application
            context.close();
        }
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
        // Load JSON data content
        String jsonContent;
        try (InputStream input = ResourceUtils.getURL(this.jsonDataPath).openStream()) {
            jsonContent = new String(input.readAllBytes(), StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new RuntimeException("Impossible de lire le fichier d'initialisation : " + this.jsonDataPath, e);
        }

        // Parse data content
        JsonDataParser parser = new JsonDataParser(
            new ObjectMapper(),
            jsonContent
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

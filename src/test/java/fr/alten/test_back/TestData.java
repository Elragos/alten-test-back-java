package fr.alten.test_back;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import fr.alten.test_back.config.JsonPropertySourceFactory;
import fr.alten.test_back.dto.product.ProductDto;
import fr.alten.test_back.dto.user.CreateUserDto;
import fr.alten.test_back.entity.Product;
import fr.alten.test_back.entity.RoleEnum;
import fr.alten.test_back.entity.User;
import fr.alten.test_back.helper.JsonDataParser;
import fr.alten.test_back.repository.ProductRepository;
import fr.alten.test_back.repository.RoleRepository;
import fr.alten.test_back.repository.UserRepository;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

/**
 * Object loading initialData.json for testing.
 *
 * @author amarechal
 */
@Component
@PropertySource(
    value = "classpath:testData.json",
    factory = JsonPropertySourceFactory.class
)
public class TestData {

    /**
     * Used environment to load JSON file.
     */
    private final Environment env;

    /**
     * Used product repository.
     */
    private final ProductRepository productRepository;
    
    /**
     * Used product repository.
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

    public TestData(
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
     * Loaded user list.
     */
    private List<CreateUserDto> users;

    /**
     * Loaded product list.
     */
    private List<ProductDto> products;

    /**
     * Load data from JSON file.
     *
     * @throws JsonProcessingException If JSON is malformed.
     */
    public void loadData() throws JsonProcessingException {
        // Load JSON data
        JsonDataParser parser = new JsonDataParser(
            new ObjectMapper(),
            this.env.getProperty("users", Object.class),
            this.env.getProperty("products", Object.class)
        );
        // Retrive loaded data.
        this.users = parser.getUsers();
        this.products = parser.getProducts();
        
        
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
    /**
     * Get loaded users.
     *
     * @return loaded users.
     */
    public List<CreateUserDto> getUsers() {
        return this.users;
    }

    /**
     * Get loaded products.
     *
     * @return loaded products.
     */
    public List<ProductDto> getProducts() {
        return this.products;
    }
}

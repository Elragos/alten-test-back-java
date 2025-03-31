package fr.alten.test_back;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import fr.alten.test_back.config.JsonPropertySourceFactory;
import fr.alten.test_back.dto.CreateUserDto;
import fr.alten.test_back.dto.ProductDto;
import fr.alten.test_back.entity.Product;
import fr.alten.test_back.entity.RoleEnum;
import fr.alten.test_back.entity.User;
import fr.alten.test_back.helper.JsonDataParser;
import fr.alten.test_back.repository.ProductRepository;
import fr.alten.test_back.repository.RoleRepository;
import fr.alten.test_back.repository.UserRepository;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

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
    @Autowired
    private Environment env;

    /**
     * Used product repository.
     */
    @Autowired
    private ProductRepository productRepository;
    
    /**
     * Used product repository.
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
     * @throws JsonProcessingException
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

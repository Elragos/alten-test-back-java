package fr.alten.test_back.config;

import fr.alten.test_back.entity.Authority;
import fr.alten.test_back.entity.AuthorityEnum;
import fr.alten.test_back.entity.Product;
import fr.alten.test_back.entity.User;
import fr.alten.test_back.helper.ProductInventoryStatus;
import fr.alten.test_back.repository.AuthorityRepository;
import fr.alten.test_back.repository.ProductRepository;
import fr.alten.test_back.repository.UserRepository;
import jakarta.annotation.PostConstruct;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * Initial data configuration. This ensures that all authorities are registered
 * in database and default admin account exists.
 *
 * @author amarechal
 */
@Configuration
public class InitialDataConfiguration {

    /**
     * JSON format initial data.
     */
    @Autowired
    private InitialDataProperties initialData;

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
     */
    @PostConstruct
    public void postConstruct() {
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
    private void loadInitialData() {
        this.initialData.getUsers();
        // For all users in initial data
        for (Map userData : this.initialData.getUsers()) {
            // Extract data from JSON
            User toAdd = new User()
                .setEmail(userData.get("email").toString())
                .setFirstname(userData.get("firstname").toString())
                .setUsername(userData.get("email").toString())
                .setPassword(this.passwordEncoder.encode(
                    userData.get("password").toString()
                ))                        
                .setAuthorities(List.of(this.authorityRepository.findByAuthority(
                    AuthorityEnum.valueOf(userData.get("authority").toString())
                ).get()));
            
            // Create user in DB if not exists
            this.createAccount(toAdd);
        }
    
        // For all products in initial data
        for (Map productData : this.initialData.getProducts()) {
            // Extract data from JSON
            Product toAdd = new Product()
                .setCode(productData.get("code").toString())
                .setName(productData.get("name").toString())
                .setDescription(productData.get("description").toString())
                .setImage(productData.get("image").toString())
                .setCategory(productData.get("category").toString())
                .setPrice(Float.valueOf(productData.get("price").toString()))
                .setQuantity(Integer.valueOf(productData.get("quantity").toString()))
                .setInternalReference(productData.get("quantity").toString())
                .setShellId(Integer.valueOf(productData.get("shellId").toString()))
                .setInventoryStatus(ProductInventoryStatus.valueOf(
                    productData.get("inventoryStatus").toString()
                ))
                .setRating(Float.valueOf(productData.get("rating").toString()));
            
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

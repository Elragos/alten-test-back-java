package fr.alten.test_back.controller;

import fr.alten.test_back.dto.CreateUserDto;
import fr.alten.test_back.dto.ProductDto;
import fr.alten.test_back.entity.Product;
import fr.alten.test_back.entity.User;
import fr.alten.test_back.entity.Wishlist;
import fr.alten.test_back.helper.AppRoutes;
import fr.alten.test_back.repository.ProductRepository;
import fr.alten.test_back.repository.UserRepository;
import fr.alten.test_back.repository.WishlistRepository;
import java.util.List;
import org.assertj.core.api.Assertions;
import static org.hamcrest.Matchers.is;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * WishlistController tests.
 *
 * @author amarechal
 */
public class WishlistControllerTests extends BaseControllerTests {
      
    /**
     * Used product repository.
     */
    @Autowired
    private ProductRepository productRepository;
    
    /**
     * Used user repository.
     */
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private WishlistRepository wishlistRepository;

    /**
     * Test get wishlist failed when not logged in.
     *
     * @throws Exception If test went wrong.
     */
    @Test
    @Order(1)
    public void getWishlistShouldFailedWhenNotLoggedIn() throws Exception {
        
        // Perform action
        this.mockMvc.perform(get(AppRoutes.WISHLIST))
            // Print result
            .andDo(print())
            // Test HTTP response is unauthorized
            .andExpect(status().isUnauthorized())
            ;
    }

    /**
     * Test wishlist sent is empty when user has no wishlist.
     *
     * @throws Exception If test went wrong.
     */
    @Test
    @Order(2)
    public void getReturnedWishlistIsEmptyWhenNotCreatedYet() throws Exception {
        // Get user
        CreateUserDto user = this.data.getUsers().get(1);
        // Get token
        String token = this.getJwtToken(user);
        // Perform action
        this.mockMvc.perform(get(AppRoutes.WISHLIST)
            .header("Authorization", "Bearer " + token)
        )
            // Print result
            .andDo(print())
            // Test HTTP response is OK
            .andExpect(status().isOk())
            // Test returned array is empty
            .andExpect(jsonPath("$.length()", is(0)))
            ;
    }
    
    /**
     * Test adding product to wishlist failed when not logged in.
     *
     * @throws Exception If test went wrong.
     */
    @Test
    @Order(3)
    public void addProductToWishlistShouldFailedWhenNotLoggedIn() throws Exception {
        
        // Perform action
        this.mockMvc.perform(post(AppRoutes.WISHLIST + "/1"))
            // Print result
            .andDo(print())
            // Test HTTP response is unauthorized
            .andExpect(status().isUnauthorized())
            ;
    }
    
    /**
     * Test adding product to wishlist is successful.
     *
     * @throws Exception If test went wrong.
     */
    @Test
    @Order(4)
    public void addProductToWishlistShouldSucceed() throws Exception {
        // Get user
        CreateUserDto userDto = this.data.getUsers().get(1);
        // Get token
        String token = this.getJwtToken(userDto);
        // Get product to add in wishlist
        ProductDto dto = this.data.getProducts().get(0);
        Product product = this.productRepository.findByCode(dto.getCode())
            .orElseThrow();
        
        // Perform action
        this.mockMvc.perform(post(AppRoutes.WISHLIST + "/" + product.getId())
            .header("Authorization", "Bearer " + token)
        )
            // Print result
            .andDo(print())
            // Test HTTP response is OK
            .andExpect(status().isOk())
            // Test returned array has 1 item
            .andExpect(jsonPath("$.length()", is(1)))
            // Test returned added product has expected code
            .andExpect(jsonPath("$[0].code", is(product.getCode())))
            ;
        
        // Get user from DB
        User user = this.userRepository.findByEmail(userDto.getEmail())
            .orElseThrow();
        // Check that wishlist has been created
        Assertions.assertThat(user.getWishlist()).isNotNull();
        // Check that product wishlist has 1 item
        Assertions.assertThat(user.getWishlist().getProducts().size())
            .isEqualTo(1);
        // Check that product in wishlist is the one expected
        Assertions.assertThat(
            user.getWishlist().getProducts().get(0).getId()
        ).isEqualTo(product.getId());
    }
    
    /**
     * Test adding unexisting product returns 404 error.
     *
     * @throws Exception If test went wrong.
     */
    @Test
    @Order(5)
    public void addUnexistingProductToWishlistShouldThrow404() throws Exception {
        // Get user
        CreateUserDto user = this.data.getUsers().get(1);
        // Get token
        String token = this.getJwtToken(user);
        // Perform action
        this.mockMvc.perform(post(AppRoutes.WISHLIST + "/0")
            .header("Authorization", "Bearer " + token)
        )
            // Print result
            .andDo(print())
            // Test HTTP response is OK
            .andExpect(status().isNotFound())
            ;
    }
    
    /**
     * Test adding product already in wishlist do nothing.
     *
     * @throws Exception If test went wrong.
     */
    @Test
    @Order(6)
    public void addProductAlreadyInWishlistDoNothing() throws Exception {
        // Get user
        CreateUserDto userDto = this.data.getUsers().get(1);
        // Get token
        String token = this.getJwtToken(userDto);
        // Get product to add in wishlist
        ProductDto dto = this.data.getProducts().get(0);
        Product product = this.productRepository.findByCode(dto.getCode())
            .orElseThrow();
        
        // Perform action
        this.mockMvc.perform(post(AppRoutes.WISHLIST + "/" + product.getId())
            .header("Authorization", "Bearer " + token)
        )
            // Print result
            .andDo(print())
            // Test HTTP response is OK
            .andExpect(status().isOk())
            // Test returned array still has 1 item
            .andExpect(jsonPath("$.length()", is(1)))
            ;
        
        // Get user from DB
        User user = this.userRepository.findByEmail(userDto.getEmail())
            .orElseThrow();
        // Check that wishlist has been created
        Assertions.assertThat(user.getWishlist()).isNotNull();
        // Check that product wishlist still has 1 item
        Assertions.assertThat(user.getWishlist().getProducts().size())
            .isEqualTo(1);
    }
    
    /**
     * Test delete product from wishlist failed when not logged in.
     *
     * @throws Exception If test went wrong.
     */
    @Test
    @Order(7)
    public void deleteProductFromWishlistShouldFailedWhenNotLoggedIn() throws Exception {
        
        // Perform action
        this.mockMvc.perform(delete(AppRoutes.WISHLIST + "/1"))
            // Print result
            .andDo(print())
            // Test HTTP response is unauthorized
            .andExpect(status().isUnauthorized())
            ;
    }
    
    /**
     * Test delete unexisting product from wishlist throw 404 error.
     *
     * @throws Exception If test went wrong.
     */
    @Test
    @Order(8)
    public void deleteUnexistingProductFromWishlistShouldThrow404() throws Exception {
        // Get user
        CreateUserDto user = this.data.getUsers().get(1);
        // Get token
        String token = this.getJwtToken(user);
        // Perform action
        this.mockMvc.perform(delete(AppRoutes.WISHLIST + "/0")
            .header("Authorization", "Bearer " + token)
        )
            // Print result
            .andDo(print())
            // Test HTTP response is unauthorized
            .andExpect(status().isNotFound())
            ;
    }
    
    /**
     * Test delete product not in wishlist do nothing.
     *
     * @throws Exception If test went wrong.
     */
    @Test
    @Order(9)
    public void deleteProductNotInWishlistDoNothing() throws Exception {
        // Get user
        CreateUserDto userDto = this.data.getUsers().get(1);
        // Get token
        String token = this.getJwtToken(userDto);
        // Get product to add in wishlist
        ProductDto dto = this.data.getProducts().get(2);
        Product product = this.productRepository.findByCode(dto.getCode())
            .orElseThrow();
        
        // Perform action
        this.mockMvc.perform(delete(AppRoutes.WISHLIST + "/" + product.getId())
            .header("Authorization", "Bearer " + token)
        )
            // Print result
            .andDo(print())
            // Test HTTP response is OK
            .andExpect(status().isOk())
            // Test returned array still has 1 item
            .andExpect(jsonPath("$.length()", is(1)))
            ;
        
        // Get user from DB
        User user = this.userRepository.findByEmail(userDto.getEmail())
            .orElseThrow();
        // Check that wishlist has been created
        Assertions.assertThat(user.getWishlist()).isNotNull();
        // Check that product wishlist still has 1 item
        Assertions.assertThat(user.getWishlist().getProducts().size())
            .isEqualTo(1);
    }
    
    /**
     * Test delete product in wishlist succeed.
     *
     * @throws Exception If test went wrong.
     */
    @Test
    @Order(10)
    public void deleteProductInWishlistSucceed() throws Exception {
        // Get user
        CreateUserDto userDto = this.data.getUsers().get(1);
        // Get token
        String token = this.getJwtToken(userDto);
        // Get product to add in wishlist
        ProductDto dto = this.data.getProducts().get(0);
        Product product = this.productRepository.findByCode(dto.getCode())
            .orElseThrow();
        
        // Perform action
        this.mockMvc.perform(delete(AppRoutes.WISHLIST + "/" + product.getId())
            .header("Authorization", "Bearer " + token)
        )
            // Print result
            .andDo(print())
            // Test HTTP response is OK
            .andExpect(status().isOk())
            // Test returned array still has 1 item
            .andExpect(jsonPath("$.length()", is(0)))
            ;
        
        // Get user from DB
        User user = this.userRepository.findByEmail(userDto.getEmail())
            .orElseThrow();
        // Check that wishlist has been created
        Assertions.assertThat(user.getWishlist()).isNotNull();
        // Check that product wishlist is now empty
        Assertions.assertThat(user.getWishlist().getProducts().size())
            .isEqualTo(0);
    }
    
    /**
     * Test that when a product is deleted, all wishlists having this product 
     * remove it.
     * 
     * @throws Exception If test went wrong.
     */
    @Test
    @Order(11)
    public void deleteProductShouldRemoveItFromAllWishlists() throws Exception {
        // Get admin
        CreateUserDto user = this.data.getUsers().get(0);
        // Get token
        String token = this.getJwtToken(user);
        // Get product from DB
        ProductDto dto = this.data.getProducts().get(0);
        Product product = this.productRepository.findByCode(dto.getCode())
            .orElseThrow();

        // Add product to wishlist
        this.mockMvc.perform(post(AppRoutes.WISHLIST + "/" + product.getId())
            .header("Authorization", "Bearer " + token)
        );
        
        // Assert that 1 wishlist has this product
        List<Wishlist> wishlists = this.wishlistRepository.findByProductsContaining(product);
        Assertions.assertThat(wishlists.size()).isEqualTo(1);
        
        // Delete product
        this.mockMvc.perform(delete(AppRoutes.PRODUCT + "/" + product.getId())
            .header("Authorization", "Bearer " + token)
        );
        // Assert that no wishlist has this product anymore
        wishlists = this.wishlistRepository.findByProductsContaining(product);
        Assertions.assertThat(wishlists.size()).isEqualTo(0);
    }
}

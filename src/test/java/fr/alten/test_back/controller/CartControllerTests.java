package fr.alten.test_back.controller;

import fr.alten.test_back.dto.AddProductToCartDto;
import fr.alten.test_back.dto.CreateUserDto;
import fr.alten.test_back.dto.ProductDto;
import fr.alten.test_back.entity.Product;
import fr.alten.test_back.helper.AppRoutes;
import fr.alten.test_back.helper.Translator;
import fr.alten.test_back.repository.ProductRepository;
import fr.alten.test_back.repository.UserRepository;
import static org.hamcrest.Matchers.is;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * CartController tests.
 *
 * @author amarechal
 */
public class CartControllerTests extends BaseControllerTests {
      
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
    

    /**
     * Test get cart failed when not logged in.
     *
     * @throws Exception If test went wrong.
     */
    @Test
    @Order(1)
    public void getCartShouldFailedWhenNotLoggedIn() throws Exception {
        
        // Perform action
        this.mockMvc.perform(get(AppRoutes.CART))
            // Print result
            .andDo(print())
            // Test HTTP response is unauthorized
            .andExpect(status().isUnauthorized())
            ;
    }

    /**
     * Test cart sent is empty when user has done nothing.
     *
     * @throws Exception If test went wrong.
     */
    @Test
    @Order(2)
    public void getCartIsEmptyWhenSessionInitialized() throws Exception {
        // Get user
        CreateUserDto user = this.data.getUsers().get(1);
        // Get token
        String token = this.getJwtToken(user);
        // Perform action
        this.mockMvc.perform(get(AppRoutes.CART)
            .header("Authorization", "Bearer " + token)
        )
            // Print result
            .andDo(print())
            // Test HTTP response is OK
            .andExpect(status().isOk())
            // Test cart item list is empty
            .andExpect(jsonPath("$.items.length()", is(0)))
            ;
    }
    
    /**
     * Test adding product to cart failed when not logged in.
     *
     * @throws Exception If test went wrong.
     */
    @Test
    @Order(3)
    public void addProductToCartShouldFailedWhenNotLoggedIn() throws Exception {
        
        // Perform action
        this.mockMvc.perform(post(AppRoutes.CART + "/1"))
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
    public void addProductToCartShouldSucceed() throws Exception {
        // Get user
        CreateUserDto userDto = this.data.getUsers().get(1);
        // Get token
        String token = this.getJwtToken(userDto);
        // Get product to add in cart
        ProductDto dto = this.data.getProducts().get(0);
        Product product = this.productRepository.findByCode(dto.getCode())
            .orElseThrow();
        // Set desired quantity
        AddProductToCartDto payload = new AddProductToCartDto().setQuantity(1);
        
        // Perform action
        this.mockMvc.perform(post(AppRoutes.CART + "/" + product.getId())
            .header("Authorization", "Bearer " + token)
            .contentType(MediaType.APPLICATION_JSON)
            .content(this.mapper.writeValueAsString(payload))
        )
            // Print result
            .andDo(print())
            // Test HTTP response is OK
            .andExpect(status().isOk())
            // Test returned array has 1 item
            .andExpect(jsonPath("$.items.length()", is(1)))
            // Test returned added product has expected code
            .andExpect(jsonPath("$.items[0].product.code", is(product.getCode())))
            // Test cart quantity is as expected
            .andExpect(jsonPath("$.items[0].quantity", is(payload.getQuantity())))
            ;
    }
    
    /**
     * Test adding unexisting product returns 404 error.
     *
     * @throws Exception If test went wrong.
     */
    @Test
    @Order(5)
    public void addUnexistingProductToCartShouldThrow404() throws Exception {
        // Get user
        CreateUserDto user = this.data.getUsers().get(1);
        // Get token
        String token = this.getJwtToken(user);
        
        // Set adding quantity
        AddProductToCartDto payload = new AddProductToCartDto().setQuantity(1);
        // Perform action
        this.mockMvc.perform(post(AppRoutes.CART + "/0")
            .header("Authorization", "Bearer " + token)
            .contentType(MediaType.APPLICATION_JSON)
            .content(this.mapper.writeValueAsString(payload))
        )
            // Print result
            .andDo(print())
            // Test HTTP response is OK
            .andExpect(status().isNotFound())
            ;
    }
    
    /**
     * Test adding product already in cart add quantity.
     *
     * @throws Exception If test went wrong.
     */
    @Test
    @Order(6)
    public void addProductAlreadyInCartAddQuantity() throws Exception {
        // Create empty session
        MockHttpSession session = new MockHttpSession();
        // Get user
        CreateUserDto userDto = this.data.getUsers().get(1);
        // Get token
        String token = this.getJwtToken(userDto);
        // Get product to add in wishlist
        ProductDto dto = this.data.getProducts().get(0);
        Product product = this.productRepository.findByCode(dto.getCode())
            .orElseThrow();
        // Set adding quantity
        AddProductToCartDto payload = new AddProductToCartDto().setQuantity(1);
        
        // Perform add product twice with same session
        this.mockMvc.perform(post(AppRoutes.CART + "/" + product.getId())
            .header("Authorization", "Bearer " + token)
            .contentType(MediaType.APPLICATION_JSON)
            .content(this.mapper.writeValueAsString(payload))
            .session(session)
        );
        this.mockMvc.perform(post(AppRoutes.CART + "/" + product.getId())
            .header("Authorization", "Bearer " + token)
            .contentType(MediaType.APPLICATION_JSON)
            .content(this.mapper.writeValueAsString(payload))
            .session(session)
        )
            // Print result
            .andDo(print())
            // Test HTTP response is OK
            .andExpect(status().isOk())
            // Test returned array has 1 item
            .andExpect(jsonPath("$.items.length()", is(1)))
            // Test returned added product has expected code
            .andExpect(jsonPath("$.items[0].quantity", is(payload.getQuantity() * 2)))
            ;
    }
    
    /**
     * Test adding more that product stock limits it to product stock.
     *
     * @throws Exception If test went wrong.
     */
    @Test
    @Order(7)
    public void addMoreThanProductStockLimitsItToStock() throws Exception {
        // Get user
        CreateUserDto userDto = this.data.getUsers().get(1);
        // Get token
        String token = this.getJwtToken(userDto);
        // Get product to add in wishlist
        ProductDto dto = this.data.getProducts().get(0);
        Product product = this.productRepository.findByCode(dto.getCode())
            .orElseThrow();
        // Set adding quantity
        AddProductToCartDto payload = new AddProductToCartDto()
            .setQuantity(product.getQuantity() + 1);
        
        // Perform add product
        this.mockMvc.perform(post(AppRoutes.CART + "/" + product.getId())
            .header("Authorization", "Bearer " + token)
            .contentType(MediaType.APPLICATION_JSON)
            .content(this.mapper.writeValueAsString(payload))
        )
            // Print result
            .andDo(print())
            // Test HTTP response is OK
            .andExpect(status().isOk())
            // Test returned array has 1 item
            .andExpect(jsonPath("$.items.length()", is(1)))
            // Test returned added product has expected code
            .andExpect(jsonPath("$.items[0].quantity", is(product.getQuantity())))
            ;
    }
    
    /**
     * Test adding more that product stock generates an error and error list is
     * reset next call.
     *
     * @throws Exception If test went wrong.
     */
    @Test
    @Order(8)
    public void addMoreThanProductStockGeneratesErrorAndErrorIsResetNextCall() 
        throws Exception {
        // Create empty session
        MockHttpSession session = new MockHttpSession();
        // Get user
        CreateUserDto userDto = this.data.getUsers().get(1);
        // Get token
        String token = this.getJwtToken(userDto);
        // Get product to add in wishlist
        ProductDto dto = this.data.getProducts().get(0);
        Product product = this.productRepository.findByCode(dto.getCode())
            .orElseThrow();
        // Set adding quantity
        AddProductToCartDto payload = new AddProductToCartDto()
            .setQuantity(product.getQuantity() + 1);
        
        // Perform add product
        this.mockMvc.perform(post(AppRoutes.CART + "/" + product.getId())
            .header("Authorization", "Bearer " + token)
            .contentType(MediaType.APPLICATION_JSON)
            .content(this.mapper.writeValueAsString(payload))
            .header("Accept-Language", "fr")
            .session(session)
        )
            // Print result
            .andDo(print())
            // Test HTTP response is OK
            .andExpect(status().isOk())
            // Test error list has 1 item
            .andExpect(jsonPath("$.errors.length()", is(1)))
            // Test error message indicates what happens
            .andExpect(jsonPath("$.errors[0]", is(
                Translator.translate(
                    "error.cart.notEnoughStock",
                    new Object[]{ product.getName(), product.getQuantity()}
                )
            )))
            ;
        // Get cart
        this.mockMvc.perform(get(AppRoutes.CART)
            .header("Authorization", "Bearer " + token)
            .session(session)
        )
            // Print result
            .andDo(print())
            // Test HTTP response is OK
            .andExpect(status().isOk())
            // Test cart error list is empty
            .andExpect(jsonPath("$.errors.length()", is(0)))
            ;
    }
    
    /**
     * Test that, when cart item has zero quantity, item is removes and a cart 
     * error is generated
     *
     * @throws Exception If test went wrong.
     */
    @Test
    @Order(9)
    public void zeroQuantityInCartRemovesProductAndGeneratesError() 
        throws Exception {
        // Create empty session
        MockHttpSession session = new MockHttpSession();
        // Get user
        CreateUserDto userDto = this.data.getUsers().get(1);
        // Get token
        String token = this.getJwtToken(userDto);
        // Get product to add in wishlist
        ProductDto dto = this.data.getProducts().get(0);
        Product product = this.productRepository.findByCode(dto.getCode())
            .orElseThrow();
        // Set adding quantity
        AddProductToCartDto payload = new AddProductToCartDto()
            .setQuantity(1);
        
        // Perform add product
        this.mockMvc.perform(post(AppRoutes.CART + "/" + product.getId())
            .header("Authorization", "Bearer " + token)
            .contentType(MediaType.APPLICATION_JSON)
            .content(this.mapper.writeValueAsString(payload))
            .header("Accept-Language", "fr")
            .session(session)
        );
        // Add the opposite quantity
        payload.setQuantity(-payload.getQuantity());
        this.mockMvc.perform(post(AppRoutes.CART + "/" + product.getId())
            .header("Authorization", "Bearer " + token)
            .contentType(MediaType.APPLICATION_JSON)
            .content(this.mapper.writeValueAsString(payload))
            .header("Accept-Language", "fr")
            .session(session)
        )
            // Print result
            .andDo(print())
            // Test HTTP response is OK
            .andExpect(status().isOk())
            // Test item list has no item
            .andExpect(jsonPath("$.items.length()", is(0)))
            // Test error list has 1 item
            .andExpect(jsonPath("$.errors.length()", is(1)))
            // Test error message indicates what happens
            .andExpect(jsonPath("$.errors[0]", is(
                Translator.translate(
                    "error.cart.productRemoved",
                    new Object[]{product.getName()}
                )
            )))
            ;
        // Get cart
        this.mockMvc.perform(get(AppRoutes.CART)
            .header("Authorization", "Bearer " + token)
            .session(session)
        )
            // Print result
            .andDo(print())
            // Test HTTP response is OK
            .andExpect(status().isOk())
            // Test cart error list is empty
            .andExpect(jsonPath("$.errors.length()", is(0)))
            ;
    }
    
    /**
     * Test delete product from cart failed when not logged in.
     *
     * @throws Exception If test went wrong.
     */
    @Test
    @Order(10)
    public void deleteProductFromCartShouldFailedWhenNotLoggedIn() throws Exception {
        
        // Perform action
        this.mockMvc.perform(delete(AppRoutes.CART + "/1"))
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
    @Order(11)
    public void deleteUnexistingProductFromCartShouldThrow404() throws Exception {
        // Get user
        CreateUserDto user = this.data.getUsers().get(1);
        // Get token
        String token = this.getJwtToken(user);

        // Perform action
        this.mockMvc.perform(delete(AppRoutes.CART + "/0")
            .header("Authorization", "Bearer " + token)
        )
            // Print result
            .andDo(print())
            // Test HTTP response is unauthorized
            .andExpect(status().isNotFound())
            ;
    }
    
    /**
     * Test delete product not in cart do nothing.
     *
     * @throws Exception If test went wrong.
     */
    @Test
    @Order(12)
    public void deleteProductNotInCartDoNothing() throws Exception {
        // Create empty session
        MockHttpSession session = new MockHttpSession();
        // Get user
        CreateUserDto userDto = this.data.getUsers().get(1);
        // Get token
        String token = this.getJwtToken(userDto);
        // Get product initially in cart
        ProductDto initialDto = this.data.getProducts().get(0);
        Product initialProduct = this.productRepository.findByCode(initialDto.getCode())
            .orElseThrow();
        // Set initial quantity
        AddProductToCartDto payload = new AddProductToCartDto()
            .setQuantity(1);
        
        // Perform add product
        this.mockMvc.perform(post(AppRoutes.CART + "/" + initialProduct.getId())
            .header("Authorization", "Bearer " + token)
            .contentType(MediaType.APPLICATION_JSON)
            .content(this.mapper.writeValueAsString(payload))
            .header("Accept-Language", "fr")
            .session(session)
        );
        
        // Get product we try to remove
        ProductDto testedDto = this.data.getProducts().get(2);
        Product testedProduct = this.productRepository.findByCode(testedDto.getCode())
            .orElseThrow();
        
        // Perform action
        this.mockMvc.perform(delete(AppRoutes.CART + "/" + testedProduct.getId())
            .header("Authorization", "Bearer " + token)
            .session(session)
        )
            // Print result
            .andDo(print())
            // Test HTTP response is OK
            .andExpect(status().isOk())
            // Test cart items still has 1 item
            .andExpect(jsonPath("$.items.length()", is(1)))
            ;
    }
    
    /**
     * Test delete product in cart succeed.
     *
     * @throws Exception If test went wrong.
     */
    @Test
    @Order(13)
    public void deleteProductInCartSucceed() throws Exception {
        // Create empty session
        MockHttpSession session = new MockHttpSession();
        // Get user
        CreateUserDto userDto = this.data.getUsers().get(1);
        // Get token
        String token = this.getJwtToken(userDto);
        // Get product to add in wishlist
        ProductDto dto = this.data.getProducts().get(0);
        Product product = this.productRepository.findByCode(dto.getCode())
            .orElseThrow();
        // Set adding quantity
        AddProductToCartDto payload = new AddProductToCartDto()
            .setQuantity(1);
        
        // Perform add product
        this.mockMvc.perform(post(AppRoutes.CART + "/" + product.getId())
            .header("Authorization", "Bearer " + token)
            .contentType(MediaType.APPLICATION_JSON)
            .content(this.mapper.writeValueAsString(payload))
            .header("Accept-Language", "fr")
            .session(session)
        );
        
        // Perform delete product
        this.mockMvc.perform(delete(AppRoutes.CART + "/" + product.getId())
            .header("Authorization", "Bearer " + token)
            .session(session)
        )
            // Print result
            .andDo(print())
            // Test HTTP response is OK
            .andExpect(status().isOk())
            // Test cart item list is empty
            .andExpect(jsonPath("$.items.length()", is(0)))
            ;
    }
}

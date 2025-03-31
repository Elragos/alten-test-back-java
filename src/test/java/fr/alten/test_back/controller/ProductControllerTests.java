package fr.alten.test_back.controller;

import fr.alten.test_back.dto.CreateUserDto;
import fr.alten.test_back.dto.ProductDto;
import fr.alten.test_back.entity.Product;
import fr.alten.test_back.helper.AppRoutes;
import fr.alten.test_back.repository.ProductRepository;
import java.util.ArrayList;
import java.util.Optional;
import org.assertj.core.api.Assertions;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.is;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrlPattern;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * LoginController tests.
 *
 * @author amarechal
 */
public class ProductControllerTests extends BaseControllerTests {

    /**
     * Used product repository.
     */
    @Autowired
    private ProductRepository productRepository;

    /**
     * Check that login is required to get all products.
     *
     * @throws Exception If test went wrong.
     */
    @Test
    @Order(1)
    public void getAllProductsShouldFailedWhenNotLoggedIn() throws Exception {
        this.mockMvc.perform(get(AppRoutes.PRODUCT))
            // Print result
            .andDo(print())
            // Test HTTP response is unauthorized
            .andExpect(status().isUnauthorized());
    }

    /**
     * Test getting all products.
     *
     * @throws Exception If test went wrong.
     */
    @Test
    @Order(2)
    public void getAllProductsShouldSucceedWith3Products() throws Exception {
        // Get user
        CreateUserDto user = this.data.getUsers().get(1);
        // Get token
        String token = this.getJwtToken(user);

        // Get test products' code
        ArrayList<String> productCodes = new ArrayList<>();
        this.productRepository.findAll().forEach(product -> 
            productCodes.add(product.getCode())
        );

        // Get product list
        this.mockMvc.perform(get(AppRoutes.PRODUCT)
            .header("Authorization", "Bearer " + token)
        )
            // Print result
            .andDo(print())
            // Test HTTP response is OK
            .andExpect(status().isOk())
            // Test list has 3 items
            .andExpect(jsonPath("$.length()", is(3)))
            // Test list contains all desired codes
            .andExpect(jsonPath("$..code",
                    containsInAnyOrder(productCodes.toArray())
            ));
    }

    /**
     * Check that login is required to get specific product.
     *
     * @throws Exception If test went wrong.
     */
    @Test
    @Order(3)
    public void getSpecificProductShouldFailedWhenNotLoggedIn() throws Exception {
        this.mockMvc.perform(get(AppRoutes.PRODUCT + "/1"))
            // Print result
            .andDo(print())
            // Test HTTP response is unauthorized
            .andExpect(status().isUnauthorized());
    }

    /**
     * Test getting specific product.
     *
     * @throws Exception If test went wrong.
     */
    @Test
    @Order(4)
    public void getSpecificProductShouldSucceed() throws Exception {
        // Get user
        CreateUserDto user = this.data.getUsers().get(1);
        // Get token
        String token = this.getJwtToken(user);

        // Get test product code
        ProductDto dto = this.data.getProducts().get(0);
        Product product = this.productRepository.findByCode(dto.getCode())
            .orElseThrow();

        // Get product details
        this.mockMvc.perform(get(AppRoutes.PRODUCT + "/" + product.getId())
            .header("Authorization", "Bearer " + token)
        )
            // Print result
            .andDo(print())
            // Test HTTP response is OK
            .andExpect(status().isOk())
            // Test sent product is the one expected
            .andExpect(jsonPath("$.code", is(product.getCode())))
            ;
    }

    /**
     * Test getting unexisting product failed with 404.
     *
     * @throws Exception If test went wrong.
     */
    @Test
    @Order(5)
    public void getUnexsitingProductShouldThrow404() throws Exception {
        // Get user
        CreateUserDto user = this.data.getUsers().get(1);
        // Get token
        String token = this.getJwtToken(user);

        // Get product details
        this.mockMvc.perform(get(AppRoutes.PRODUCT + "/0")
            .header("Authorization", "Bearer " + token)
        )
            // Print result
            .andDo(print())
            // Test HTTP response is 404
            .andExpect(status().isNotFound());
    }

    /**
     * Check that login is required to create product.
     *
     * @throws Exception If test went wrong.
     */
    @Test
    @Order(6)
    public void createProductShouldFailedWhenNotLoggedIn() throws Exception {
        this.mockMvc.perform(post(AppRoutes.PRODUCT))
            // Print result
            .andDo(print())
            // Test HTTP response is unauthorized
            .andExpect(status().isUnauthorized());
    }
    
    /**
     * Check that admin is required to create product.
     *
     * @throws Exception If test went wrong.
     */
    @Test
    @Order(7)
    public void createProductShouldFailedWhenNotAdmin() throws Exception {
        // Get user
        CreateUserDto user = this.data.getUsers().get(1);
        // Get token
        String token = this.getJwtToken(user);
        ProductDto productData = this.data.getProducts().get(0);

        // Get product details
        this.mockMvc.perform(post(AppRoutes.PRODUCT)
            .header("Authorization", "Bearer " + token)
            .contentType(MediaType.APPLICATION_JSON)
            .content(this.mapper.writeValueAsString(productData))
        )
            // Print result
            .andDo(print())
            // Test HTTP response is forbidden
            .andExpect(status().isForbidden());
    }
    
    /**
     * Check that admin can create new product.
     *
     * @throws Exception If test went wrong.
     */
    @Test
    @Order(8)
    public void createProductShouldSucceedWhenAdmin() throws Exception {
        // Get user
        CreateUserDto user = this.data.getUsers().get(0);
        // Get token
        String token = this.getJwtToken(user);
        // Get existing product
        ProductDto productData = this.data.getProducts().get(0);
        // Change code to avoid conflicts
        String productCode = "Test creation";
        productData.setCode(productCode);

        // Create product details
        MvcResult response = this.mockMvc.perform(post(AppRoutes.PRODUCT)
            .header("Authorization", "Bearer " + token)
            .contentType(MediaType.APPLICATION_JSON)
            .content(this.mapper.writeValueAsString(productData))
        )
            // Print result
            .andDo(print())
            // Test HTTP response is Created
            .andExpect(status().isCreated())
            // Test that we are redirected to product details URL
            .andExpect(redirectedUrlPattern(AppRoutes.PRODUCT + "/{id:[0-9]+}"))
            // Get response
            .andReturn()
            ;
        // Access redirected URL
        this.mockMvc.perform(get(response.getResponse().getRedirectedUrl())
            .header("Authorization", "Bearer " + token)
        )
            // Print result
            .andDo(print())
            // Test HTTP response is OK
            .andExpect(status().isOk())
            // Test sent product is the one expected
            .andExpect(jsonPath("$.code", is(productCode)))
        ;
        
    }
    
    /**
     * Check that duplicate products with same code is forbidden.
     *
     * @throws Exception If test went wrong.
     */
    @Test
    @Order(9)
    public void createExistingProductShouldFailed() throws Exception {
        // Get user
        CreateUserDto user = this.data.getUsers().get(0);
        // Get token
        String token = this.getJwtToken(user);
        // Get existing product
        ProductDto productData = this.data.getProducts().get(0);

        // Try creating product
        this.mockMvc.perform(post(AppRoutes.PRODUCT)
            .header("Authorization", "Bearer " + token)
            .contentType(MediaType.APPLICATION_JSON)
            .content(this.mapper.writeValueAsString(productData))
        )
            // Print result
            .andDo(print())
            // Test HTTP response is bad request
            .andExpect(status().isBadRequest())
            ;                
    }
    
    /**
     * Check that login is required to update product.
     *
     * @throws Exception If test went wrong.
     */
    @Test
    @Order(10)
    public void updateProductShouldFailedWhenNotLoggedIn() throws Exception {
        this.mockMvc.perform(patch(AppRoutes.PRODUCT + "/1"))
            // Print result
            .andDo(print())
            // Test HTTP response is unauthorized
            .andExpect(status().isUnauthorized());
    }
    
    /**
     * Check that admin is required to update product.
     *
     * @throws Exception If test went wrong.
     */
    @Test
    @Order(11)
    public void updateProductShouldFailedWhenNotAdmin() throws Exception {
        // Get user
        CreateUserDto user = this.data.getUsers().get(1);
        // Get token
        String token = this.getJwtToken(user);
        ProductDto productData = this.data.getProducts().get(0);

        // Try updating product
        this.mockMvc.perform(patch(AppRoutes.PRODUCT + "/1")
            .header("Authorization", "Bearer " + token)
            .contentType(MediaType.APPLICATION_JSON)
            .content(this.mapper.writeValueAsString(productData))
        )
            // Print result
            .andDo(print())
            // Test HTTP response is forbidden
            .andExpect(status().isForbidden());
    }
    
    /**
     * Check that admin can update product.
     *
     * @throws Exception If test went wrong.
     */
    @Test
    @Order(12)
    public void updateProductShouldSucceedWhenAdmin() throws Exception {
        // Get user
        CreateUserDto user = this.data.getUsers().get(0);
        // Get token
        String token = this.getJwtToken(user);
        // Get product from DB
        ProductDto dto = this.data.getProducts().get(0);
        Product product = this.productRepository.findByCode(dto.getCode())
            .orElseThrow();
        // Update only description
        String newDescription = "Description updated";
        ProductDto productData = new ProductDto().setDescription(newDescription);

        // Update product details
        this.mockMvc.perform(patch(AppRoutes.PRODUCT + "/" + product.getId())
            .header("Authorization", "Bearer " + token)
            .contentType(MediaType.APPLICATION_JSON)
            .content(this.mapper.writeValueAsString(productData))
        )
            // Print result
            .andDo(print())
            // Test HTTP response is OK
            .andExpect(status().isOk())
            // Test description has been updated
            .andExpect(jsonPath("$.description", is(newDescription)))
            // Test other fields has not been changed
            .andExpect(jsonPath("$.code", is(product.getCode())))
            ;
        
        // Reload product from DB
        product = this.productRepository.findById(product.getId()).orElseThrow();
        // Check that product description has been updated
        Assertions.assertThat(product.getDescription()).isEqualTo(newDescription);
        // Check that product code is not null
        Assertions.assertThat(product.getCode()).isNotEmpty();
    }
    
    /**
     * Check that admin can update product code if product code is not used.
     *
     * @throws Exception If test went wrong.
     */
    @Test
    @Order(13)
    public void updateProductCodeShouldSucceedIfNotUsed() throws Exception {
        // Get user
        CreateUserDto user = this.data.getUsers().get(0);
        // Get token
        String token = this.getJwtToken(user);
        // Get product from DB
        ProductDto dto = this.data.getProducts().get(0);
        Product product = this.productRepository.findByCode(dto.getCode())
            .orElseThrow();;
        // Update only code
        String newCode = "Code updated";
        ProductDto productData = new ProductDto().setCode(newCode);

        // Update product details
        this.mockMvc.perform(patch(AppRoutes.PRODUCT + "/" + product.getId())
            .header("Authorization", "Bearer " + token)
            .contentType(MediaType.APPLICATION_JSON)
            .content(this.mapper.writeValueAsString(productData))
        )
            // Print result
            .andDo(print())
            // Test HTTP response is OK
            .andExpect(status().isOk())
            // Test code has been updated
            .andExpect(jsonPath("$.code", is(newCode)))
            ;
        
        // Reload product from DB
        product = this.productRepository.findById(product.getId()).orElseThrow();
        // Check that product code has been updated
        Assertions.assertThat(product.getCode()).isEqualTo(newCode);
    }
    
    /**
     * Check that admin can update product code if product updated 
     * is the one using that code.
     *
     * @throws Exception If test went wrong.
     */
    @Test
    @Order(14)
    public void updateProductCodeShouldSucceedIfSameProduct() throws Exception {
        // Get user
        CreateUserDto user = this.data.getUsers().get(0);
        // Get token
        String token = this.getJwtToken(user);
        // Get product from DB
        ProductDto dto = this.data.getProducts().get(0);
        Product product = this.productRepository.findByCode(dto.getCode())
            .orElseThrow();
        // Update only code
        String newCode = product.getCode();
        ProductDto productData = new ProductDto().setCode(newCode);

        // Update product details
        this.mockMvc.perform(patch(AppRoutes.PRODUCT + "/" + product.getId())
            .header("Authorization", "Bearer " + token)
            .contentType(MediaType.APPLICATION_JSON)
            .content(this.mapper.writeValueAsString(productData))
        )
            // Print result
            .andDo(print())
            // Test HTTP response is OK
            .andExpect(status().isOk())
            // Test code has been updated
            .andExpect(jsonPath("$.code", is(newCode)))
            ;
        
        // Reload product from DB
        product = this.productRepository.findById(product.getId()).orElseThrow();
        // Check that product code has been updated
        Assertions.assertThat(product.getCode()).isEqualTo(newCode);
    }
    
    /**
     * Check that admin cannot update product code if code used by 
     * another product.
     *
     * @throws Exception If test went wrong.
     */
    @Test
    @Order(15)
    public void updateProductCodeShouldFailedIfAlreadyUsed() throws Exception {
        // Get user
        CreateUserDto user = this.data.getUsers().get(0);
        // Get token
        String token = this.getJwtToken(user);
        // Get products from DB
        ProductDto updatedDto = this.data.getProducts().get(0);
        Product updatedProduct = this.productRepository
            .findByCode(updatedDto.getCode()).orElseThrow();
        ProductDto collidingDto = this.data.getProducts().get(1);
        Product collidingProduct = this.productRepository
            .findByCode(collidingDto.getCode()).orElseThrow();
        // Update only code
        String newCode = collidingProduct.getCode();
        ProductDto productData = new ProductDto().setCode(newCode);

        // Update product details
        this.mockMvc.perform(patch(AppRoutes.PRODUCT + "/" + updatedProduct.getId())
            .header("Authorization", "Bearer " + token)
            .contentType(MediaType.APPLICATION_JSON)
            .content(this.mapper.writeValueAsString(productData))
        )
            // Print result
            .andDo(print())
            // Test HTTP response is bad request
            .andExpect(status().isBadRequest())
            ;
    }
    
    /**
     * Check that admin cannot update product if product id does not exists.
     *
     * @throws Exception If test went wrong.
     */
    @Test
    @Order(16)
    public void updateProductShouldFailedIfNotExists() throws Exception {
        // Get user
        CreateUserDto user = this.data.getUsers().get(0);
        // Get token
        String token = this.getJwtToken(user);
        // Update only code
        String newCode = "Test";
        ProductDto productData = new ProductDto().setCode(newCode);

        // Update product details
        this.mockMvc.perform(patch(AppRoutes.PRODUCT + "/0")
            .header("Authorization", "Bearer " + token)
            .contentType(MediaType.APPLICATION_JSON)
            .content(this.mapper.writeValueAsString(productData))
        )
            // Print result
            .andDo(print())
            // Test HTTP response is 404
            .andExpect(status().isNotFound())
            ;
    }
    
    /**
     * Check that login is required to delete product.
     *
     * @throws Exception If test went wrong.
     */
    @Test
    @Order(17)
    public void deleteProductShouldFailedWhenNotLoggedIn() throws Exception {
        this.mockMvc.perform(delete(AppRoutes.PRODUCT + "/1"))
            // Print result
            .andDo(print())
            // Test HTTP response is unauthorized
            .andExpect(status().isUnauthorized());
    }
    
    /**
     * Check that admin is required to delete product.
     *
     * @throws Exception If test went wrong.
     */
    @Test
    @Order(18)
    public void deleteProductShouldFailedWhenNotAdmin() throws Exception {
        // Get user
        CreateUserDto user = this.data.getUsers().get(1);
        // Get token
        String token = this.getJwtToken(user);

        // Try updating product
        this.mockMvc.perform(delete(AppRoutes.PRODUCT + "/1")
            .header("Authorization", "Bearer " + token)
        )
            // Print result
            .andDo(print())
            // Test HTTP response is forbidden
            .andExpect(status().isForbidden());
    }
    
    /**
     * Check that admin cannot delete product if product id does not exists.
     *
     * @throws Exception If test went wrong.
     */
    @Test
    @Order(19)
    public void deleteProductShouldFailedIfNotExists() throws Exception {
        // Get user
        CreateUserDto user = this.data.getUsers().get(0);
        // Get token
        String token = this.getJwtToken(user);

        // Try deleting product
        this.mockMvc.perform(delete(AppRoutes.PRODUCT + "/0")
            .header("Authorization", "Bearer " + token)
        )
            // Print result
            .andDo(print())
            // Test HTTP response is 404
            .andExpect(status().isNotFound())
            ;
    }
    
    /**
     * Check that admin can delete product .
     *
     * @throws Exception If test went wrong.
     */
    @Test
    @Order(20)
    public void deleteProductShouldSuccedIfAdmin() throws Exception {
        // Get admin
        CreateUserDto user = this.data.getUsers().get(0);
        // Get token
        String token = this.getJwtToken(user);
        // Get product from DB
        ProductDto dto = this.data.getProducts().get(0);
        Product product = this.productRepository.findByCode(dto.getCode())
            .orElseThrow();

        // Delete product
        this.mockMvc.perform(delete(AppRoutes.PRODUCT + "/" + product.getId())
            .header("Authorization", "Bearer " + token)
        )
            // Print result
            .andDo(print())
            // Test HTTP response is OK
            .andExpect(status().isOk())
            // Test deleted product has same code as expected
            .andExpect(jsonPath("$.code", is(product.getCode())))
            ;
        
        // Try reloading product
        Optional<Product> deleted = this.productRepository
            .findById(product.getId());
        
        // Check that product does not exists anymore
        Assertions.assertThat(deleted.isEmpty());
    }
}

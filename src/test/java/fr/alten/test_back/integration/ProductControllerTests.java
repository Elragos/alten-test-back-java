package fr.alten.test_back.integration;

import fr.alten.test_back.dto.product.ProductDto;
import fr.alten.test_back.dto.user.CreateUserDto;
import fr.alten.test_back.entity.Product;
import fr.alten.test_back.helper.AppRoutes;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * LoginController tests.
 *
 * @author amarechal
 */
public class ProductControllerTests extends BaseControllerTests {

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
                /*
            .andExpect(jsonPath("$..code",
                containsInAnyOrder(this.data.getProducts().stream().map(ProductDto::code).toList())
            ))
            */
            ;
    }

    /**
     * Check that login is required to get specific product.
     *
     * @throws Exception If test went wrong.
     */
    @Test
    @Order(3)
    public void getSpecificProductShouldFailedWhenNotLoggedIn() throws Exception {
        this.mockMvc.perform(get(AppRoutes.PRODUCT + "/0"))
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
        ProductDto dto = this.data.getProducts().getFirst();

        // Get product details
        this.mockMvc.perform(get(AppRoutes.PRODUCT + "/" + dto.code())
            .header("Authorization", "Bearer " + token)
        )
            // Print result
            .andDo(print())
            // Test HTTP response is OK
            .andExpect(status().isOk())
            // Test sent product is the one expected
            .andExpect(jsonPath("$.code", is(dto.code())))
            ;
    }

    /**
     * Test getting non-existing product failed with 404.
     *
     * @throws Exception If test went wrong.
     */
    @Test
    @Order(5)
    public void getNonExsitingProductShouldThrow404() throws Exception {
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
        ProductDto productData = this.data.getProducts().getFirst();

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
        CreateUserDto user = this.data.getUsers().getFirst();
        // Get token
        String token = this.getJwtToken(user);
        // Get existing product
        ProductDto productData = this.data.getProducts().getFirst();
        Product product = new Product(productData);
        product.setCode("Test creation");
        productData = ProductDto.generate(product);

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
            .andExpect(redirectedUrlPattern(AppRoutes.PRODUCT + "/{code:[A-Za-z+]+}"))
            // Get response
            .andReturn()
            ;
        // Access redirected URL
        String url = Objects.requireNonNull(response.getResponse().getRedirectedUrl());
        url = URLDecoder.decode(url, StandardCharsets.UTF_8);
        this.mockMvc.perform(get(url)
            .header("Authorization", "Bearer " + token)
        )
            // Print result
            .andDo(print())
            // Test HTTP response is OK
            .andExpect(status().isOk())
            // Test sent product is the one expected
            .andExpect(jsonPath("$.code", is(productData.code())))
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
        CreateUserDto user = this.data.getUsers().getFirst();
        // Get token
        String token = this.getJwtToken(user);
        // Get existing product
        ProductDto productData = this.data.getProducts().getFirst();

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
        ProductDto productData = this.data.getProducts().getFirst();

        // Try updating product
        this.mockMvc.perform(patch(AppRoutes.PRODUCT + "/" + productData.code())
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
        CreateUserDto user = this.data.getUsers().getFirst();
        // Get token
        String token = this.getJwtToken(user);
        // Get product from DB
        ProductDto dto = this.data.getProducts().getFirst();
        Product product = new Product(dto);
        // Update only description
        product.setDescription("Description updated");
        ProductDto productData = ProductDto.generate(product);

        // Update product details
        this.mockMvc.perform(patch(AppRoutes.PRODUCT + "/" + productData.code())
            .header("Authorization", "Bearer " + token)
            .contentType(MediaType.APPLICATION_JSON)
            .content(this.mapper.writeValueAsString(productData))
        )
            // Print result
            .andDo(print())
            // Test HTTP response is OK
            .andExpect(status().isOk())
            // Test description has been updated
            .andExpect(jsonPath("$.description", is(productData.description())))
            // Test other fields has not been changed
            .andExpect(jsonPath("$.code", is(productData.code())))
            ;
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
        CreateUserDto user = this.data.getUsers().getFirst();
        // Get token
        String token = this.getJwtToken(user);
        // Get product from DB
        ProductDto dto = this.data.getProducts().getFirst();
        Product product = new Product(dto);
        // Update only code
        product.setCode("Code updated");
        ProductDto productData = ProductDto.generate(product);

        // Update product details
        this.mockMvc.perform(patch(AppRoutes.PRODUCT + "/" + dto.code())
            .header("Authorization", "Bearer " + token)
            .contentType(MediaType.APPLICATION_JSON)
            .content(this.mapper.writeValueAsString(productData))
        )
            // Print result
            .andDo(print())
            // Test HTTP response is OK
            .andExpect(status().isOk())
            // Test code has been updated
            .andExpect(jsonPath("$.code", is(productData.code())))
            ;
    }
    
    /**
     * Check that admin can update product if product updated
     * is the one using that code.
     *
     * @throws Exception If test went wrong.
     */
    @Test
    @Order(14)
    public void updateProductCodeShouldSucceedIfSameProduct() throws Exception {
        // Get user
        CreateUserDto user = this.data.getUsers().getFirst();
        // Get token
        String token = this.getJwtToken(user);
        // Get product from DB
        ProductDto dto = this.data.getProducts().getFirst();

        // Update product details
        this.mockMvc.perform(patch(AppRoutes.PRODUCT + "/" + dto.code())
            .header("Authorization", "Bearer " + token)
            .contentType(MediaType.APPLICATION_JSON)
            .content(this.mapper.writeValueAsString(dto))
        )
            // Print result
            .andDo(print())
            // Test HTTP response is OK
            .andExpect(status().isOk())
            // Test code has been updated
            .andExpect(jsonPath("$.code", is(dto.code())))
            ;
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
        CreateUserDto user = this.data.getUsers().getFirst();
        // Get token
        String token = this.getJwtToken(user);
        // Get products from DB
        ProductDto updatedDto = this.data.getProducts().getFirst();
        ProductDto collidingDto = this.data.getProducts().get(1);
        // Update only code
        Product updatedProduct = new Product(updatedDto);
        updatedProduct.setCode(collidingDto.code());
        ProductDto productData = ProductDto.generate(updatedProduct);

        // Update product details
        this.mockMvc.perform(patch(AppRoutes.PRODUCT + "/" + updatedDto.code())
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
        CreateUserDto user = this.data.getUsers().getFirst();
        // Get token
        String token = this.getJwtToken(user);
        // Get product
        ProductDto updatedDto = this.data.getProducts().getFirst();

        // Update product details
        this.mockMvc.perform(patch(AppRoutes.PRODUCT + "/0")
            .header("Authorization", "Bearer " + token)
            .contentType(MediaType.APPLICATION_JSON)
            .content(this.mapper.writeValueAsString(updatedDto))
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
        CreateUserDto user = this.data.getUsers().getFirst();
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
     * Check that admin can delete product.
     *
     * @throws Exception If test went wrong.
     */
    @Test
    @Order(20)
    public void deleteProductShouldSucceedIfAdmin() throws Exception {
        // Get admin
        CreateUserDto user = this.data.getUsers().getFirst();
        // Get token
        String token = this.getJwtToken(user);
        // Get product from DB
        ProductDto dto = this.data.getProducts().getFirst();
        // Delete product
        this.mockMvc.perform(delete(AppRoutes.PRODUCT + "/" + dto.code())
            .header("Authorization", "Bearer " + token)
        )
            // Print result
            .andDo(print())
            // Test HTTP response is OK
            .andExpect(status().isOk())
            // Test deleted product has same code as expected
            .andExpect(jsonPath("$.code", is(dto.code())))
            ;
    }
}

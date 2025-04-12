package fr.alten.test_back.integration;

import fr.alten.test_back.dto.product.ProductDto;
import fr.alten.test_back.dto.user.CreateUserDto;
import fr.alten.test_back.helper.AppRoutes;

import org.junit.jupiter.api.Test;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
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
     * Test get wishlist failed when not logged in.
     *
     * @throws Exception If test went wrong.
     */
    @Test
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
    public void getReturnedWishlistIsEmptyWhenNotCreatedYet() throws Exception {
        // Get user
        CreateUserDto user = this.testData.getUsers().get(1);
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
    public void addProductToWishlistShouldSucceed() throws Exception {
        // Get user
        CreateUserDto userDto = this.testData.getUsers().get(1);
        // Get token
        String token = this.getJwtToken(userDto);
        // Get product to add in wishlist
        ProductDto dto = this.testData.getProducts().getFirst();
        
        // Perform action
        this.mockMvc.perform(post(AppRoutes.WISHLIST + "/" + dto.code())
            .header("Authorization", "Bearer " + token)
        )
            // Print result
            .andDo(print())
            // Test HTTP response is OK
            .andExpect(status().isOk())
            // Test returned array has 1 item
            .andExpect(jsonPath("$.length()", is(1)))
            // Test returned added product has expected code
            .andExpect(jsonPath("$[0].code", is(dto.code())))
            ;

         // Get wishlist from server
        this.mockMvc.perform(get(AppRoutes.WISHLIST)
            .header("Authorization", "Bearer " + token)
        )
            // Print result
            .andDo(print())
            // Test HTTP response is OK
            .andExpect(status().isOk())
            // Test returned array has 1 item
            .andExpect(jsonPath("$.length()", is(1)))
            // Test returned added product has expected code
            .andExpect(jsonPath("$[0].code", is(dto.code())))
            ;
    }
    
    /**
     * Test adding non-existing product returns 404 error.
     *
     * @throws Exception If test went wrong.
     */
    @Test
    public void addNonExistingProductToWishlistShouldThrow404() throws Exception {
        // Get user
        CreateUserDto user = this.testData.getUsers().get(1);
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
    public void addProductAlreadyInWishlistDoNothing() throws Exception {
        // Get user
        CreateUserDto userDto = this.testData.getUsers().get(1);
        // Get token
        String token = this.getJwtToken(userDto);
        // Get product to add in wishlist
        ProductDto dto = this.testData.getProducts().getFirst();
        
        // Perform action twice
        this.mockMvc.perform(post(AppRoutes.WISHLIST + "/" + dto.code())
            .header("Authorization", "Bearer " + token)
        );
        this.mockMvc.perform(post(AppRoutes.WISHLIST + "/" + dto.code())
                .header("Authorization", "Bearer " + token)
        )
            // Print result
            .andDo(print())
            // Test HTTP response is OK
            .andExpect(status().isOk())
            // Test returned array still has 1 item
            .andExpect(jsonPath("$.length()", is(1)))
            ;
    }
    
    /**
     * Test delete product from wishlist failed when not logged in.
     *
     * @throws Exception If test went wrong.
     */
    @Test
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
     * Test delete non-existing product from wishlist throw 404 error.
     *
     * @throws Exception If test went wrong.
     */
    @Test
    public void deleteNonExistingProductFromWishlistShouldThrow404() throws Exception {
        // Get user
        CreateUserDto user = this.testData.getUsers().get(1);
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
    public void deleteProductNotInWishlistDoNothing() throws Exception {
        // Get user
        CreateUserDto userDto = this.testData.getUsers().get(1);
        // Get token
        String token = this.getJwtToken(userDto);
        // Get products
        ProductDto addedDto = this.testData.getProducts().getFirst();
        ProductDto deletedDto = this.testData.getProducts().getLast();

        // Add first product to wishlist
        this.mockMvc.perform(post(AppRoutes.WISHLIST + "/" + addedDto.code())
            .header("Authorization", "Bearer " + token)
        );
        // Delete second product from wishlist
        this.mockMvc.perform(delete(AppRoutes.WISHLIST + "/" + deletedDto.code())
            .header("Authorization", "Bearer " + token)
        )
            // Print result
            .andDo(print())
            // Test HTTP response is OK
            .andExpect(status().isOk())
            // Test returned array still has 1 item
            .andExpect(jsonPath("$.length()", is(1)))
            ;

        // Get wishlist from server
        this.mockMvc.perform(get(AppRoutes.WISHLIST)
            .header("Authorization", "Bearer " + token)
        )
            // Print result
            .andDo(print())
            // Test HTTP response is OK
            .andExpect(status().isOk())
            // Test returned array has 1 item
            .andExpect(jsonPath("$.length()", is(1)))
            // Test returned product code matches the one expected
            .andExpect(jsonPath("$[0].code", is(addedDto.code())))
        ;
    }
    
    /**
     * Test delete product in wishlist succeed.
     *
     * @throws Exception If test went wrong.
     */
    @Test
    public void deleteProductInWishlistSucceed() throws Exception {
        // Get user
        CreateUserDto userDto = this.testData.getUsers().get(1);
        // Get token
        String token = this.getJwtToken(userDto);
        // Get product to remove from wishlist
        ProductDto dto = this.testData.getProducts().getFirst();

        // Add product to wishlist
        this.mockMvc.perform(post(AppRoutes.WISHLIST + "/" + dto.code())
            .header("Authorization", "Bearer " + token)
        );
        // Perform action
        this.mockMvc.perform(delete(AppRoutes.WISHLIST + "/" + dto.code())
            .header("Authorization", "Bearer " + token)
        )
            // Print result
            .andDo(print())
            // Test HTTP response is OK
            .andExpect(status().isOk())
            // Test returned array has no item
            .andExpect(jsonPath("$.length()", is(0)))
            ;
    }
    
    /**
     * Test delete product when wishlist is not created do nothing.
     *
     * @throws Exception If test went wrong.
     */
    @Test
    public void deleteProductWhenNoWishlistDoNothing() throws Exception {
        // Get user
        CreateUserDto userDto = this.testData.getUsers().getFirst();
        // Get token
        String token = this.getJwtToken(userDto);
        // Get product to add in wishlist
        ProductDto dto = this.testData.getProducts().getFirst();
        
        // Perform action
        this.mockMvc.perform(delete(AppRoutes.WISHLIST + "/" + dto.code())
            .header("Authorization", "Bearer " + token)
        )
            // Print result
            .andDo(print())
            // Test HTTP response is OK
            .andExpect(status().isOk())
            // Test returned array has no item
            .andExpect(jsonPath("$.length()", is(0)))
            ;
    }

    /**
     * Test that deleting a product from DB removes it from all wishlists.
     * @throws Exception If something went wrong.
     */
    @Test
    public void deleteProductRemovesItFromAllWishlists() throws Exception {
        // Get users
        CreateUserDto adminDto = this.testData.getUsers().getFirst();
        CreateUserDto userDto = this.testData.getUsers().getLast();
        // Get test product
        ProductDto productDto = this.testData.getProducts().getFirst();

        // Get JWT tokens
        String adminToken = this.getJwtToken(adminDto);
        String userToken = this.getJwtToken(userDto);

        // Add product to admin wishlist
        this.mockMvc.perform(post(AppRoutes.WISHLIST + "/" + productDto.code())
            .header("Authorization", "Bearer " + adminToken)
        );
        // Add product to user wishlist
        this.mockMvc.perform(post(AppRoutes.WISHLIST + "/" + productDto.code())
            .header("Authorization", "Bearer " + userToken)
        );
        // Delete product
        this.mockMvc.perform(delete(AppRoutes.PRODUCT + "/" + productDto.code())
            .header("Authorization", "Bearer " + adminToken)
        );
        // Get admin wishlist from server
        this.mockMvc.perform(get(AppRoutes.WISHLIST)
            .header("Authorization", "Bearer " + adminToken)
        )
            // Print result
            .andDo(print())
            // Test HTTP response is OK
            .andExpect(status().isOk())
            // Test returned array has no item
            .andExpect(jsonPath("$.length()", is(0)))
        ;
        // Get user wishlist from server
        this.mockMvc.perform(get(AppRoutes.WISHLIST)
            .header("Authorization", "Bearer " + userToken)
        )
            // Print result
            .andDo(print())
            // Test HTTP response is OK
            .andExpect(status().isOk())
            // Test returned array has no item
            .andExpect(jsonPath("$.length()", is(0)))
        ;
    }
}

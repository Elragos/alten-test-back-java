package fr.alten.test_back.controller;

import fr.alten.test_back.dto.product.ProductDto;
import fr.alten.test_back.entity.Product;
import fr.alten.test_back.entity.User;
import fr.alten.test_back.entity.Wishlist;
import fr.alten.test_back.helper.AppRoutes;
import fr.alten.test_back.service.ProductService;
import fr.alten.test_back.service.UserService;
import fr.alten.test_back.service.WishlistService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Controller handling wishlist interactions.
 *
 * @author AMarechal
 */
@RestController
@RequestMapping(AppRoutes.WISHLIST)
public class WishlistController {

    /**
     * Used product service.
     */
    private final ProductService productService;
    /**
     * Used user service.
     */
    private final UserService userService;

    /**
     * Used wishlist service.
     */
    private final WishlistService wishlistService;

    /**
     * Initialize controller.
     * @param wishlistService Used wishlist service.
     */
    public WishlistController(
        ProductService productService,
        UserService userService,
        WishlistService wishlistService

    ){
        this.productService = productService;
        this.userService = userService;
        this.wishlistService = wishlistService;
    }

    /**
     * List all available products in current user's wishlist.
     *
     * @return Current user's wishlist product list.
     */
    @GetMapping
    public ResponseEntity<List<ProductDto>> listProducts() {
        // Get authenticated user
        User user = this.userService.getAuthenticatedUser();

        // Get user wishlist products
        List<Product> products = this.wishlistService.getWishlist(user).getProducts();

        // Return updated wishlist product list
        return ResponseEntity.ok(this.mapToDto(products));
    }

    /**
     * Add product to wishlist.
     *
     * @param code Desired product code.
     * @return Updated wishlist product list.
     */
    @PostMapping("/{code}")
    public ResponseEntity<List<ProductDto>> addProduct(@PathVariable String code) {
        // Get authenticated user
        User user = this.userService.getAuthenticatedUser();

        // Find desired product
        Product product = this.productService.getByCode(code);

        // Add product to wishlist
        Wishlist wishlist = this.wishlistService.addProduct(user, product);

        // Return updated wishlist
        return ResponseEntity.ok(this.mapToDto(wishlist.getProducts()));
    }

    /**
     * Remove product from wishlist.
     *
     * @param code Desired product code.
     * @return Updated wishlist product list.
     */
    @DeleteMapping("/{code}")
    public ResponseEntity<List<ProductDto>> removeProduct(@PathVariable String code) {
        // Get authenticated user
        User user = this.userService.getAuthenticatedUser();

        // Find desired product
        Product product = this.productService.getByCode(code);

        // Remove product from wishlist
        Wishlist wishlist = this.wishlistService.removeProduct(user, product);

        // Return updated wishlist product list
        return ResponseEntity.ok(this.mapToDto(wishlist.getProducts()));
    }

    /**
     * Map product list to product dto list.
     * @param products Product list.
     * @return Generated product dto list.
     */
    private List<ProductDto> mapToDto(List<Product> products){
        if (products == null){
            return List.of();
        }

        return products.stream().map(ProductDto::generate).collect(Collectors.toList());
    }
}

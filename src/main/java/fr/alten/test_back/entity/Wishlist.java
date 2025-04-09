package fr.alten.test_back.entity;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Entity representing a user wishlist.
 *
 * @author Amarechal
 */
@Entity
public class Wishlist {

    /**
     * Wishlist DB id.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    /**
     * Wishlist products.
     */
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
        name = "wishlist_product",
        joinColumns = @JoinColumn(name = "wishlist_id"),
        inverseJoinColumns = @JoinColumn(name = "product_id")
    )
    private List<Product> products;

    /**
     * Get wishlist DB ID.
     *
     * @return Wishlist DB id.
     */
    public Integer getId() {
        return this.id;
    }

    /**
     * Set wishlist DB id.
     *
     * @param id New wishlist DB id.
     * @return self
     */
    public Wishlist setId(Integer id) {
        this.id = id;
        return this;
    }
    
    /**
     * Get wishlist products.
     *
     * @return Wishlist products.
     */
    public List<Product> getProducts() {
        return this.products;
    }

    /**
     * Set wishlist products.
     *
     * @param products New wishlist products.
     * @return self
     */
    public Wishlist setProducts(List<Product> products) {
        this.products = products;
        return this;
    }

    /**
     * Add product to wishlist. If already in wishlist, nothing happens.
     *
     * @param toAdd Product to add.
     * @return self
     */
    public Wishlist addProduct(Product toAdd) {
        // If Product list empty
        if (this.products == null) {
            // Create it
            this.products = new ArrayList<>();
        }
        // If product not already in wishlist
        if (!this.products.contains(toAdd)) {
            // Add it
            this.products.add(toAdd);
        }

        return this;
    }

    /**
     * Remove product to wishlist. If not in wishlist, nothing happens.
     *
     * @param toRemove Product to remove.
     * @return self
     */
    public Wishlist removeProduct(Product toRemove) {
        // If product list exists and contains product to remove
        if (this.products != null) {
            // Remove product from list
            this.products.remove(toRemove);
        }

        return this;
    }
}

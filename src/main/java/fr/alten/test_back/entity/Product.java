package fr.alten.test_back.entity;

import fr.alten.test_back.dto.ProductDto;
import fr.alten.test_back.helper.ProductInventoryStatus;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import java.util.Date;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

/**
 * Entity representing an app product.
 *
 * @author AMarechal
 */
@Entity
public class Product {

    /**
     * Product DB id.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    private String code;
    private String name;
    private String description;
    private String image;
    private String category;
    private float price;
    private int quantity;
    private String internalReference;
    private int shellId;
    private ProductInventoryStatus inventoryStatus;
    private float rating;
    /**
     * Product creation date.
     */
    @CreationTimestamp
    @Column(updatable = false, name = "created_at")
    private Date createdAt;
    /**
     * User last update date.
     */
    @UpdateTimestamp
    @Column(name = "updated_at")
    private Date updatedAt;

    public Product(){
        
    }
    
    /**
     * Create new product from DTO.
     *
     * @param newProductData
     */
    public Product(ProductDto newProductData) {
        this.code = newProductData.getCode();
        this.name = newProductData.getName();
        this.description = newProductData.getDescription();
        this.image = newProductData.getImage();
        this.category = newProductData.getCategory();
        this.price = newProductData.getPrice();
        this.quantity = newProductData.getQuantity();
        this.internalReference = newProductData.getInternalReference();
        this.shellId = newProductData.getShellId();
        this.inventoryStatus = newProductData.getInventoryStatus();
        this.rating = newProductData.getRating();
    }

    public Integer getId() {
        return id;
    }

    public Product setId(Integer id) {
        this.id = id;
        return this;
    }

    public String getCode() {
        return code;
    }

    public Product setCode(String code) {
        this.code = code;
        return this;
    }

    public String getName() {
        return name;
    }

    public Product setName(String name) {
        this.name = name;
        return this;
    }

    public String getDescription() {
        return description;
    }

    public Product setDescription(String description) {
        this.description = description;
        return this;
    }

    public String getImage() {
        return image;
    }

    public Product setImage(String image) {
        this.image = image;
        return this;
    }

    public String getCategory() {
        return category;
    }

    public Product setCategory(String category) {
        this.category = category;
        return this;
    }

    public float getPrice() {
        return price;
    }

    public Product setPrice(float price) {
        this.price = price;
        return this;
    }

    public int getQuantity() {
        return quantity;
    }

    public Product setQuantity(int quantity) {
        this.quantity = quantity;
        return this;
    }

    public String getInternalReference() {
        return internalReference;
    }

    public Product setInternalReference(String internalReference) {
        this.internalReference = internalReference;
        return this;
    }

    public int getShellId() {
        return shellId;
    }

    public Product setShellId(int shellId) {
        this.shellId = shellId;
        return this;
    }

    public ProductInventoryStatus getInventoryStatus() {
        return inventoryStatus;
    }

    public Product setInventoryStatus(ProductInventoryStatus inventoryStatus) {
        this.inventoryStatus = inventoryStatus;
        return this;
    }

    public float getRating() {
        return rating;
    }

    public Product setRating(float rating) {
        this.rating = rating;
        return this;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public Product setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
        return this;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public Product setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
        return this;
    }

    /**
     * Update product from DTO (if field is null, ignore it).
     *
     * @param updateInfo Updated product info.
     * @return Updated product.
     */
    public Product updateFromDto(ProductDto updateInfo) {
        if (updateInfo.getCategory() != null) {
            this.setCategory(updateInfo.getCategory());
        }
        if (updateInfo.getCode() != null) {
            this.setCode(updateInfo.getCode());
        }
        if (updateInfo.getDescription() != null) {
            this.setDescription(updateInfo.getDescription());
        }
        if (updateInfo.getImage() != null) {
            this.setImage(updateInfo.getImage());
        }
        if (updateInfo.getInternalReference() != null) {
            this.setInternalReference(updateInfo.getInternalReference());
        }
        if (updateInfo.getInventoryStatus() != null) {
            this.setInventoryStatus(updateInfo.getInventoryStatus());
        }
        if (updateInfo.getName() != null) {
            this.setName(updateInfo.getName());
        }
        if (updateInfo.getPrice() != null) {
            this.setPrice(updateInfo.getPrice());
        }
        if (updateInfo.getQuantity() != null) {
            this.setQuantity(updateInfo.getQuantity());
        }
        if (updateInfo.getRating() != null) {
            this.setRating(updateInfo.getRating());
        }
        if (updateInfo.getShellId() != null) {
            this.setShellId(updateInfo.getShellId());
        }

        return this;
    }
}

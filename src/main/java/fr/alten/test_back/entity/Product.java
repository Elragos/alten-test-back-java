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

    /**
     * Product code.
     */
    private String code;

    /**
     * Product name.
     */
    private String name;

    /**
     * Product description.
     */
    private String description;

    /**
     * Product image URL.
     */
    private String image;

    /**
     * Product category.
     */
    private String category;

    /**
     * Product price.
     */
    private Float price;

    /**
     * Product quantity.
     */
    private Integer quantity;

    /**
     * Product internal reference.
     */
    private String internalReference;

    /**
     * Product shell Id.
     */
    private Integer shellId;

    /**
     * Product inventory status.
     */
    private ProductInventoryStatus inventoryStatus;

    /**
     * Product rating.
     */
    private Float rating;

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

    /**
     * Default constructor, mandatory for JPA.
     */
    public Product() {
    }

    /**
     * Create new product from DTO.
     *
     * @param newProductData Product data.
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

    /**
     * Get product DB ID.
     *
     * @return Product DB ID.
     */
    public Integer getId() {
        return id;
    }

    /**
     * Set product DB ID.
     *
     * @param id Product DB ID.
     * @return self.
     */
    public Product setId(Integer id) {
        this.id = id;
        return this;
    }

    /**
     * Get product code.
     *
     * @return Product code.
     */
    public String getCode() {
        return code;
    }

    /**
     * Set product code.
     *
     * @param code New product code.
     * @return self.
     */
    public Product setCode(String code) {
        this.code = code;
        return this;
    }

    /**
     * Get product name.
     *
     * @return Product name.
     */
    public String getName() {
        return name;
    }

    /**
     * Set product name.
     *
     * @param name New product name.
     * @return self.
     */
    public Product setName(String name) {
        this.name = name;
        return this;
    }

    /**
     * Get product description.
     *
     * @return Product description.
     */
    public String getDescription() {
        return description;
    }

    /**
     * Set product DB ID.
     *
     * @param description New product description.
     * @return self.
     */
    public Product setDescription(String description) {
        this.description = description;
        return this;
    }

    /**
     * Get product image URL.
     *
     * @return Product image URL.
     */
    public String getImage() {
        return image;
    }

    /**
     * Set product image URL.
     *
     * @param image New product image URL.
     * @return self.
     */
    public Product setImage(String image) {
        this.image = image;
        return this;
    }

    /**
     * Get product DB ID.
     *
     * @return Product DB ID.
     */
    public String getCategory() {
        return category;
    }

    /**
     * Set product category.
     *
     * @param category New product category.
     * @return self.
     */
    public Product setCategory(String category) {
        this.category = category;
        return this;
    }

    /**
     * Get product price.
     *
     * @return Product price.
     */
    public float getPrice() {
        return price;
    }

    /**
     * Set product price.
     *
     * @param price New product price.
     * @return self.
     */
    public Product setPrice(Float price) {
        this.price = price;
        return this;
    }

    /**
     * Get product quantity.
     *
     * @return Product quantity.
     */
    public Integer getQuantity() {
        return quantity;
    }

    /**
     * Set product quantity.
     *
     * @param quantity New product quantity.
     * @return self.
     */
    public Product setQuantity(Integer quantity) {
        this.quantity = quantity;
        return this;
    }

    /**
     * Get product internal reference.
     *
     * @return Product internal reference.
     */
    public String getInternalReference() {
        return internalReference;
    }

    /**
     * Set product internal reference.
     *
     * @param internalReference New product internal reference.
     * @return self.
     */
    public Product setInternalReference(String internalReference) {
        this.internalReference = internalReference;
        return this;
    }

    /**
     * Get product shell ID.
     *
     * @return Product shell ID.
     */
    public Integer getShellId() {
        return shellId;
    }

    /**
     * Set product shell ID.
     *
     * @param shellId New product shell ID.
     * @return self.
     */
    public Product setShellId(Integer shellId) {
        this.shellId = shellId;
        return this;
    }

    /**
     * Get product inventory status.
     *
     * @return Product inventory status.
     */
    public ProductInventoryStatus getInventoryStatus() {
        return inventoryStatus;
    }

    /**
     * Set product inventory status.
     *
     * @param inventoryStatus New product inventory status.
     * @return self.
     */
    public Product setInventoryStatus(ProductInventoryStatus inventoryStatus) {
        this.inventoryStatus = inventoryStatus;
        return this;
    }

    /**
     * Get product rating.
     *
     * @return Product rating.
     */
    public Float getRating() {
        return rating;
    }

    /**
     * Set product rating.
     *
     * @param rating New product rating.
     * @return self.
     */
    public Product setRating(Float rating) {
        this.rating = rating;
        return this;
    }

    /**
     * Get product creation date.
     *
     * @return Product creation date.
     */
    public Date getCreatedAt() {
        return createdAt;
    }

    /**
     * Set product creation date.
     *
     * @param createdAt New product creation date.
     * @return self.
     */
    public Product setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
        return this;
    }

    /**
     * Get product last update date.
     *
     * @return Product last update date.
     */
    public Date getUpdatedAt() {
        return updatedAt;
    }

    /**
     * Set product last update date.
     *
     * @param updatedAt New product last update date.
     * @return self.
     */
    public Product setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
        return this;
    }

    /**
     * Update product from DTO (if field is null, ignore it).
     *
     * @param updateInfo Updated product info.
     * @return self.
     */
    public Product updateFromDto(ProductDto updateInfo) {
        // Set new category if DTO has one
        if (updateInfo.getCategory() != null) {
            this.setCategory(updateInfo.getCategory());
        }
        // Set new code if DTO has one
        if (updateInfo.getCode() != null) {
            this.setCode(updateInfo.getCode());
        }
        // Set new description if DTO has one
        if (updateInfo.getDescription() != null) {
            this.setDescription(updateInfo.getDescription());
        }
        // Set new image URL if DTO has one
        if (updateInfo.getImage() != null) {
            this.setImage(updateInfo.getImage());
        }
        // Set new internal reference if DTO has one
        if (updateInfo.getInternalReference() != null) {
            this.setInternalReference(updateInfo.getInternalReference());
        }
        // Set new inventory status if DTO has one
        if (updateInfo.getInventoryStatus() != null) {
            this.setInventoryStatus(updateInfo.getInventoryStatus());
        }
        // Set new name if DTO has one
        if (updateInfo.getName() != null) {
            this.setName(updateInfo.getName());
        }
        // Set new price if DTO has one
        if (updateInfo.getPrice() != null) {
            this.setPrice(updateInfo.getPrice());
        }
        // Set new quantity if DTO has one
        if (updateInfo.getQuantity() != null) {
            this.setQuantity(updateInfo.getQuantity());
        }
        // Set new rating if DTO has one
        if (updateInfo.getRating() != null) {
            this.setRating(updateInfo.getRating());
        }
        // Set new shell ID if DTO has one
        if (updateInfo.getShellId() != null) {
            this.setShellId(updateInfo.getShellId());
        }

        // Set new update date.
        this.setUpdatedAt(new Date());

        return this;
    }
}

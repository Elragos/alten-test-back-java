package fr.alten.test_back.dto;

import fr.alten.test_back.helper.ProductInventoryStatus;

/**
 * Product DTO, used to create or update product. If a field is null, then it
 * will be ignored.
 *
 * @author Amarechal
 */
public class ProductDto {

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
    private float rating;

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
    public ProductDto setCode(String code) {
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
    public ProductDto setName(String name) {
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
    public ProductDto setDescription(String description) {
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
    public ProductDto setImage(String image) {
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
    public ProductDto setCategory(String category) {
        this.category = category;
        return this;
    }

    /**
     * Get product price.
     *
     * @return Product price.
     */
    public Float getPrice() {
        return price;
    }

    /**
     * Set product price.
     *
     * @param price New product price.
     * @return self.
     */
    public ProductDto setPrice(Float price) {
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
    public ProductDto setQuantity(Integer quantity) {
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
    public ProductDto setInternalReference(String internalReference) {
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
    public ProductDto setShellId(Integer shellId) {
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
    public ProductDto setInventoryStatus(ProductInventoryStatus inventoryStatus) {
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
    public ProductDto setRating(Float rating) {
        this.rating = rating;
        return this;
    }
}

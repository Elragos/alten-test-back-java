/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package fr.alten.test_back.dto;

import fr.alten.test_back.helper.ProductInventoryStatus;

/**
 *
 * @author User
 */
public class ProductDto {

    private String code;
    private String name;
    private String description;
    private String image;
    private String category;
    private Float price;
    private Integer quantity;
    private String internalReference;
    private Integer shellId;
    private ProductInventoryStatus inventoryStatus;
    private Float rating;

    public String getCode() {
        return code;
    }

    public ProductDto setCode(String code) {
        this.code = code;
        return this;
    }

    public String getName() {
        return name;
    }

    public ProductDto setName(String name) {
        this.name = name;
        return this;
    }

    public String getDescription() {
        return description;
    }

    public ProductDto setDescription(String description) {
        this.description = description;
        return this;
    }

    public String getImage() {
        return image;
    }

    public ProductDto setImage(String image) {
        this.image = image;
        return this;
    }

    public String getCategory() {
        return category;
    }

    public ProductDto setCategory(String category) {
        this.category = category;
        return this;
    }

    public Float getPrice() {
        return price;
    }

    public ProductDto setPrice(Float price) {
        this.price = price;
        return this;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public ProductDto setQuantity(Integer quantity) {
        this.quantity = quantity;
        return this;
    }

    public String getInternalReference() {
        return internalReference;
    }

    public ProductDto setInternalReference(String internalReference) {
        this.internalReference = internalReference;
        return this;
    }

    public Integer getShellId() {
        return shellId;
    }

    public ProductDto setShellId(Integer shellId) {
        this.shellId = shellId;
        return this;
    }

    public ProductInventoryStatus getInventoryStatus() {
        return inventoryStatus;
    }

    public ProductDto setInventoryStatus(ProductInventoryStatus inventoryStatus) {
        this.inventoryStatus = inventoryStatus;
        return this;
    }

    public Float getRating() {
        return rating;
    }

    public ProductDto setRating(Float rating) {
        this.rating = rating;
        return this;
    }

}

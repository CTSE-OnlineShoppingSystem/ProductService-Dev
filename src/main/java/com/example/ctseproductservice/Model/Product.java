package com.example.ctseproductservice.Model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document("Product")
public class Product {

    @Id
    private String productId;
    private String productName;
    private float productPrice;
    private float availability;
    private String unit;
    private String img_filename;
    private String img_fileId;
    private String category;

    public Product() {
    }

    public Product(String productId, String productName, float productPrice, float availability, String unit, String img_filename, String img_fileId,String category) {
        this.productId = productId;
        this.productName = productName;
        this.productPrice = productPrice;
        this.availability = availability;
        this.unit = unit;
        this.img_filename = img_filename;
        this.img_fileId = img_fileId;
        this.category = category;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public float getProductPrice() {
        return productPrice;
    }

    public void setProductPrice(float productPrice) {
        this.productPrice = productPrice;
    }

    public float getAvailability() {
        return availability;
    }

    public void setAvailability(float availability) {
        this.availability = availability;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getImg_filename() {
        return img_filename;
    }

    public void setImg_filename(String img_filename) {
        this.img_filename = img_filename;
    }

    public String getImg_fileId() {
        return img_fileId;
    }

    public void setImg_fileId(String img_fileId) {
        this.img_fileId = img_fileId;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }
}


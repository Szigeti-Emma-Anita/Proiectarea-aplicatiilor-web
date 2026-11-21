package com.essential.essential.model;

import java.math.BigDecimal;

/**
 * O linie din cos: un produs + cantitatea aleasa.
 * Pastram o copie a datelor produsului (nume, pret, poza) ca sa nu depindem de entitatea JPA.
 */
public class CartItem {

    private final Long productId;
    private final String name;
    private final String imageUrl;
    private BigDecimal unitPrice;
    private int quantity;

    public CartItem(Long productId, String name, String imageUrl, BigDecimal unitPrice, int quantity) {
        this.productId = productId;
        this.name = name;
        this.imageUrl = imageUrl;
        this.unitPrice = unitPrice;
        this.quantity = quantity;
    }

    public Long getProductId() {
        return productId;
    }

    public String getName() {
        return name;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public BigDecimal getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(BigDecimal unitPrice) {
        this.unitPrice = unitPrice;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public BigDecimal getSubtotal() {
        return unitPrice.multiply(BigDecimal.valueOf(quantity));
    }
}

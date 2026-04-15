package com.microservices.cartservice.event;

public class CartEvent {

    private Long cartId;
    private Long productId;
    private int quantity;

    public CartEvent() {}

    public CartEvent(Long cartId, Long productId, int quantity) {
        this.cartId = cartId;
        this.productId = productId;
        this.quantity = quantity;
    }

    public Long getCartId() { return cartId; }
    public Long getProductId() { return productId; }
    public int getQuantity() { return quantity; }

    public void setCartId(Long cartId) { this.cartId = cartId; }
    public void setProductId(Long productId) { this.productId = productId; }
    public void setQuantity(int quantity) { this.quantity = quantity; }
}
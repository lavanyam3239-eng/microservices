package com.microservices.cartservice.entity;

import jakarta.persistence.*;
import com.fasterxml.jackson.annotation.JsonBackReference;

@Entity
@Table(name = "cart_item")
public class CartItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long productId;
    private int quantity;

    @ManyToOne
    @JoinColumn(name = "cart_id")
    @JsonBackReference
    private Cart cart;

    // GETTERS
    public Long getId() { return id; }
    public Long getProductId() { return productId; }
    public int getQuantity() { return quantity; }
    public Cart getCart() { return cart; }

    // SETTERS
    public void setId(Long id) { this.id = id; }
    public void setProductId(Long productId) { this.productId = productId; }
    public void setQuantity(int quantity) { this.quantity = quantity; }

    // 🔥 THIS IS THE FIX
    public void setCart(Cart cart) {
        this.cart = cart;
    }
}
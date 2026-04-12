package com.microservices.cartservice.entity;

import jakarta.persistence.*;
import java.util.List;
import com.fasterxml.jackson.annotation.JsonManagedReference;

@Entity
@Table(name = "cart")
public class Cart {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long userId;

    // 🔥 IMPORTANT: Relationship with CartItem
    @OneToMany(mappedBy = "cart", cascade = CascadeType.ALL)
    @JsonManagedReference
    private List<CartItem> items;

    // =====================
    // GETTERS
    // =====================
    public Long getId() {
        return id;
    }

    public Long getUserId() {
        return userId;
    }

    public List<CartItem> getItems() {   // 🔥 THIS FIXES ERROR
        return items;
    }

    // =====================
    // SETTERS
    // =====================
    public void setId(Long id) {
        this.id = id;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public void setItems(List<CartItem> items) {
        this.items = items;
    }
}
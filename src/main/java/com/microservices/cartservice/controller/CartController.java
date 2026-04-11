package com.microservices.cartservice.controller;

import com.microservices.cartservice.entity.Cart;
import com.microservices.cartservice.entity.CartItem;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/cart")
public class CartController {

    // Temporary in-memory storage (we will replace with DB later)
    private List<Cart> carts = new ArrayList<>();
    private List<CartItem> cartItems = new ArrayList<>();

    // ===============================
    // CREATE CART
    // ===============================
    @PostMapping
    public Cart createCart(@RequestBody Cart cart) {
        carts.add(cart);
        return cart;
    }

    // ===============================
    // GET ALL CARTS
    // ===============================
    @GetMapping
    public List<Cart> getAllCarts() {
        return carts;
    }

    // ===============================
    // ADD ITEM TO CART
    // ===============================
    @PostMapping("/item")
    public CartItem addItem(@RequestBody CartItem item) {
        cartItems.add(item);
        return item;
    }

    // ===============================
    // GET ALL CART ITEMS
    // ===============================
    @GetMapping("/items")
    public List<CartItem> getAllItems() {
        return cartItems;
    }
}
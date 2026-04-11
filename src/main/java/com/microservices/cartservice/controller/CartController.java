package com.microservices.cartservice.controller;

import com.microservices.cartservice.entity.Cart;
import com.microservices.cartservice.entity.CartItem;
import com.microservices.cartservice.service.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/cart")
@RequiredArgsConstructor
public class CartController {

    private final CartService cartService;

    // ============================
    // CREATE CART
    // ============================
    @PostMapping
    public Cart createCart(@RequestBody Cart cart) {
        return cartService.createCart(cart);
    }

    // ============================
    // GET ALL CARTS
    // ============================
    @GetMapping
    public List<Cart> getAllCarts() {
        return cartService.getAllCarts();
    }

    // ============================
    // GET CART BY ID
    // ============================
    @GetMapping("/{id}")
    public Cart getCartById(@PathVariable Long id) {
        return cartService.getCartById(id);
    }

    // ============================
    // GET CART BY USER ID
    // ============================
    @GetMapping("/user/{userId}")
    public Cart getCartByUserId(@PathVariable Long userId) {
        return cartService.getCartByUserId(userId);
    }

    // ============================
    // ADD ITEM TO CART
    // ============================
    @PostMapping("/item")
    public CartItem addItem(@RequestBody CartItem cartItem) {
        return cartService.addItemToCart(cartItem);
    }

    // ============================
    // GET ITEMS BY CART ID
    // ============================
    @GetMapping("/{cartId}/items")
    public List<CartItem> getItems(@PathVariable Long cartId) {
        return cartService.getItemsByCartId(cartId);
    }

    // ============================
    // REMOVE ITEM
    // ============================
    @DeleteMapping("/item/{itemId}")
    public String removeItem(@PathVariable Long itemId) {
        cartService.removeItem(itemId);
        return "Item removed successfully";
    }

    // ============================
    // CLEAR CART
    // ============================
    @DeleteMapping("/{cartId}")
    public String clearCart(@PathVariable Long cartId) {
        cartService.clearCart(cartId);
        return "Cart cleared successfully";
    }

    // ============================
    // TEST API (VERY IMPORTANT)
    // ============================
    @GetMapping("/test")
    public String test() {
        return "Cart API Working!";
    }
}
package com.microservices.cartservice.controller;

import com.microservices.cartservice.dto.CartItemRequest;
import com.microservices.cartservice.entity.Cart;
import com.microservices.cartservice.entity.CartItem;
import com.microservices.cartservice.service.CartService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/cart")
@RequiredArgsConstructor
public class CartController {

    private final CartService cartService;

    // ✅ CREATE CART (FIXED - NO REQUEST BODY)
    @PostMapping
    public Cart createCart() {
        return cartService.createCart(new Cart());
    }

    // ✅ ADD ITEM TO CART
    @PostMapping("/add-item")
    public CartItem addItem(@Valid @RequestBody CartItemRequest request) {
        return cartService.addItemToCart(
                request.getCartId(),
                request.getProductId(),
                request.getQuantity()
        );
    }

    // ✅ GET CART BY ID
    @GetMapping("/{cartId}")
    public Cart getCart(@PathVariable Long cartId) {
        return cartService.getCart(cartId);
    }

    // ✅ DELETE ITEM FROM CART
    @DeleteMapping("/item/{itemId}")
    public String deleteItem(@PathVariable Long itemId) {
        return cartService.deleteItem(itemId);
    }
}
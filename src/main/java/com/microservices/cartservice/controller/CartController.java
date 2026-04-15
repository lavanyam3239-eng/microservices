package com.microservices.cartservice.controller;

import com.microservices.cartservice.dto.CartItemRequest;
import com.microservices.cartservice.entity.Cart;
import com.microservices.cartservice.entity.CartItem;
import com.microservices.cartservice.service.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/cart")
@RequiredArgsConstructor
public class CartController {

    private final CartService cartService;

    // ✅ CREATE CART
    @PostMapping
    public Cart createCart(@RequestBody Cart cart) {
        return cartService.createCart(cart);
    }

    // ✅ ADD ITEM (DTO)
    @PostMapping("/add-item")
    public CartItem addItem(@RequestBody CartItemRequest request) {
        return cartService.addItemToCart(
                request.getCartId(),
                request.getProductId(),
                request.getQuantity()
        );
    }

    // ✅ GET CART
    @GetMapping("/{cartId}")
    public Cart getCart(@PathVariable Long cartId) {
        return cartService.getCart(cartId);
    }

    // ✅ DELETE ITEM
    @DeleteMapping("/item/{itemId}")
    public String deleteItem(@PathVariable Long itemId) {
        return cartService.deleteItem(itemId);
    }
}
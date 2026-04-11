package com.microservices.cartservice.service;

import com.microservices.cartservice.entity.Cart;
import com.microservices.cartservice.repository.CartRepository;
import com.microservices.cartservice.repository.CartItemRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CartService {

    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;

    // Constructor Injection
    public CartService(CartRepository cartRepository,
                       CartItemRepository cartItemRepository) {
        this.cartRepository = cartRepository;
        this.cartItemRepository = cartItemRepository;
    }

    // Save Cart
    public Cart saveCart(Cart cart) {
        return cartRepository.save(cart);
    }

    // Get Cart by User ID
    public List<Cart> getCartByUserId(Long userId) {
        return cartRepository.findAll(); // (we will improve later)
    }
}
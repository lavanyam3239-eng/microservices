package com.microservices.cartservice.service;

import com.microservices.cartservice.entity.Cart;
import com.microservices.cartservice.entity.CartItem;
import com.microservices.cartservice.repository.CartItemRepository;
import com.microservices.cartservice.repository.CartRepository;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.*;

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

    // ✅ CREATE CART
    public Cart createCart(Cart cart) {
        if (cart.getItems() != null && !cart.getItems().isEmpty()) {
            cart.getItems().forEach(item -> item.setCart(cart));
        }
        return cartRepository.save(cart);
    }

    // ✅ GET ALL CARTS
    public List<Cart> getAllCarts() {
        return cartRepository.findAll();
    }

    // ✅ GET CART BY ID
    public Cart getCartById(Long id) {
        return cartRepository.findById(id).orElse(null);
    }

    // ✅ GET CART BY USER ID (keep ONLY ONE)
    public List<Cart> getCartByUserId(Long userId) {
        return cartRepository.findByUserId(userId);
    }

    // ✅ ADD ITEM
    public CartItem addItemToCart(CartItem item) {
        return cartItemRepository.save(item);
    }

    // ✅ GET ITEMS BY CART ID (keep ONLY ONE)
    public List<CartItem> getItemsByCartId(Long cartId) {
        return cartItemRepository.findByCartId(cartId);
    }

    // ✅ REMOVE ITEM
    public void removeItem(Long itemId) {
        cartItemRepository.deleteById(itemId);
    }

    // ✅ CLEAR CART
    public void clearCart(Long cartId) {
        cartRepository.deleteById(cartId);
    }

    // ✅ PAGINATION
    public Page<Cart> getCarts(int page, int size, String sortBy) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy));
        return cartRepository.findAll(pageable);
    }
}
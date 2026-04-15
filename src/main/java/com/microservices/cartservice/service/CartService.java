package com.microservices.cartservice.service;

import com.microservices.cartservice.dto.ProductResponse;
import com.microservices.cartservice.entity.Cart;
import com.microservices.cartservice.entity.CartItem;
import com.microservices.cartservice.repository.CartItemRepository;
import com.microservices.cartservice.repository.CartRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
public class CartService {

    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final WebClient webClient;

    // ✅ Constructor Injection
    public CartService(CartRepository cartRepository,
                       CartItemRepository cartItemRepository,
                       WebClient webClient) {

        this.cartRepository = cartRepository;
        this.cartItemRepository = cartItemRepository;
        this.webClient = webClient;
    }

    // ✅ CREATE CART
    public Cart createCart(Cart cart) {
        return cartRepository.save(cart);
    }

    // 🔥 CALL PRODUCT SERVICE USING WEBCLIENT
    public ProductResponse validateProduct(Long productId) {

        ProductResponse product = webClient.get()
                .uri("/products/" + productId)
                .retrieve()
                .bodyToMono(ProductResponse.class)
                .block();

        if (product == null) {
            throw new RuntimeException("Product not found");
        }

        return product;
    }

    // 🔥 ADD ITEM TO CART
    public CartItem addItemToCart(Long cartId, Long productId, int quantity) {

        // ✅ Validate product
        ProductResponse product = validateProduct(productId);

        // ✅ Check stock
        if (product.getStock() < quantity) {
            throw new RuntimeException("Product out of stock!");
        }

        // ✅ Fetch cart
        Cart cart = cartRepository.findById(cartId)
                .orElseThrow(() -> new RuntimeException("Cart not found"));

        // ✅ Create item
        CartItem item = new CartItem();
        item.setProductId(productId);
        item.setQuantity(quantity);
        item.setCart(cart);

        // ✅ Save item
        return cartItemRepository.save(item);
    }

    // ✅ GET CART
    public Cart getCart(Long cartId) {
        return cartRepository.findById(cartId)
                .orElseThrow(() -> new RuntimeException("Cart not found"));
    }

    // ✅ DELETE ITEM
    public String deleteItem(Long itemId) {
        cartItemRepository.deleteById(itemId);
        return "Item removed successfully";
    }
}
package com.microservices.cartservice.service;

import java.util.concurrent.CompletableFuture;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.microservices.cartservice.repository.CartRepository;
import com.microservices.cartservice.repository.CartItemRepository;
import com.microservices.cartservice.entity.Cart;
import com.microservices.cartservice.entity.CartItem;
import com.microservices.cartservice.event.CartEvent;
import com.microservices.cartservice.dto.ProductResponse;
import com.microservices.cartservice.kafka.KafkaProducerService;

@Service
public class CartService {

    // 🔥 Logger (IMPORTANT)
    private static final Logger log = LoggerFactory.getLogger(CartService.class);

    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final KafkaProducerService kafkaProducerService;

    public CartService(CartRepository cartRepository,
                       CartItemRepository cartItemRepository,
                       KafkaProducerService kafkaProducerService) {
        this.cartRepository = cartRepository;
        this.cartItemRepository = cartItemRepository;
        this.kafkaProducerService = kafkaProducerService;
    }

    // ✅ CREATE CART
    public Cart createCart(Cart cart) {
        log.info("🛒 Creating cart for userId={}", cart.getUserId());
        return cartRepository.save(cart);
    }

    // 🔥 TEMP PRODUCT VALIDATION
    public ProductResponse validateProduct(Long productId) {

        log.info("🔍 Fetching product for productId={}", productId);

        ProductResponse product = new ProductResponse();
        product.setId(productId);
        product.setName("Dummy Product");
        product.setPrice(1000);
        product.setStock(10);

        return product;
    }

    // 🔥 ADD ITEM
    public CartItem addItemToCart(Long cartId, Long productId, int quantity) {

        log.info("📥 API Call → cartId={}, productId={}, quantity={}",
                cartId, productId, quantity);

        // Async call
        ProductResponse product = CompletableFuture
                .supplyAsync(() -> validateProduct(productId))
                .join();

        // ✅ Stock validation
        if (product.getStock() < quantity) {
            log.error("❌ Product out of stock for productId={}", productId);
            throw new IllegalArgumentException("Product out of stock!");
        }

        // ✅ Fetch cart
        Cart cart = cartRepository.findById(cartId)
                .orElseThrow(() -> {
                    log.error("❌ Cart not found for cartId={}", cartId);
                    return new RuntimeException("Cart not found");
                });

        // ✅ Create item
        CartItem item = new CartItem();
        item.setProductId(productId);
        item.setQuantity(quantity);
        item.setCart(cart);

        CartItem savedItem = cartItemRepository.save(item);

        // 🔥 Kafka event
        CartEvent event = new CartEvent(cartId, productId, quantity);
        kafkaProducerService.sendEvent(event);

        log.info("🚀 Kafka Event Sent → {}", event);

        return savedItem;
    }

    // ✅ GET CART
    public Cart getCart(Long cartId) {
        log.info("📦 Fetching cart for cartId={}", cartId);

        return cartRepository.findById(cartId)
                .orElseThrow(() -> {
                    log.error("❌ Cart not found for cartId={}", cartId);
                    return new RuntimeException("Cart not found");
                });
    }

    // ✅ DELETE ITEM
    public String deleteItem(Long itemId) {
        log.info("🗑 Deleting item with id={}", itemId);
        cartItemRepository.deleteById(itemId);
        return "Item removed successfully";
    }
}
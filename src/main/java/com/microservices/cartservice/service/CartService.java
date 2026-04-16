package com.microservices.cartservice.service;

import java.util.concurrent.CompletableFuture;

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

    // ✅ Dependencies
    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final KafkaProducerService kafkaProducerService;

    // ✅ Constructor Injection
    public CartService(CartRepository cartRepository,
                       CartItemRepository cartItemRepository,
                       KafkaProducerService kafkaProducerService) {
        this.cartRepository = cartRepository;
        this.cartItemRepository = cartItemRepository;
        this.kafkaProducerService = kafkaProducerService;
    }

    // ✅ CREATE CART
    public Cart createCart(Cart cart) {
        return cartRepository.save(cart);
    }

    // 🔥 TEMP PRODUCT VALIDATION (Mock)
    public ProductResponse validateProduct(Long productId) {

        System.out.println("Fetching product - Thread: " + Thread.currentThread().getName());

        // Temporary mock (until WebClient is fixed)
        ProductResponse product = new ProductResponse();
        product.setId(productId);
        product.setName("Dummy Product");
        product.setPrice(1000);
        product.setStock(10); // dummy stock

        return product;
    }

    // 🔥 ASYNC ADD ITEM (CompletableFuture)
    public CartItem addItemToCart(Long cartId, Long productId, int quantity) {

        // 🔥 Async Task → Fetch Product
        CompletableFuture<ProductResponse> productFuture =
                CompletableFuture.supplyAsync(() -> validateProduct(productId));

        // 🔥 Wait for async completion
        CompletableFuture.allOf(productFuture).join();

        // 👉 Get result
        ProductResponse product = productFuture.join();

        // ✅ Validate stock
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

        // ✅ Save to DB
        CartItem savedItem = cartItemRepository.save(item);

        // 🔥 Kafka Event
        CartEvent event = new CartEvent(
                cart.getId(),
                productId,
                quantity
        );

        kafkaProducerService.sendEvent(event);

        System.out.println("Kafka Event Sent: " + event);

        return savedItem;
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
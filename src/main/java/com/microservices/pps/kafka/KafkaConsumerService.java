package com.microservices.pps.kafka;

import com.microservices.pps.entity.Product;
import com.microservices.pps.event.CartEvent;
import com.microservices.pps.repository.ProductRepository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class KafkaConsumerService {

    // 🔥 Logger
    private static final Logger log = LoggerFactory.getLogger(KafkaConsumerService.class);

    private final ProductRepository productRepository;

    public KafkaConsumerService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @KafkaListener(topics = "cart-topic", groupId = "inventory-group")
    public void consume(CartEvent event) {

        // ✅ Log received event
        log.info("📥 Received Kafka Event → cartId={}, productId={}, quantity={}",
                event.getCartId(), event.getProductId(), event.getQuantity());

        try {
            // ✅ Fetch product
            Product product = productRepository.findById(event.getProductId())
                    .orElseThrow(() -> new RuntimeException("Product not found"));

            // ✅ Calculate new stock
            int updatedStock = product.getStock() - event.getQuantity();

            if (updatedStock < 0) {
                log.error("❌ Stock cannot be negative for productId={}", event.getProductId());
                return;
            }

            // ✅ Update DB
            product.setStock(updatedStock);
            productRepository.save(product);

            // ✅ Success log
            log.info("✅ Stock updated for productId={}, newStock={}",
                    product.getId(), updatedStock);

        } catch (Exception e) {
            // ❌ Error log
            log.error("❌ Error processing Kafka event", e);
        }
    }
}
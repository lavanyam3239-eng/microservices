package com.microservices.pps.kafka;

import com.microservices.pps.entity.Product;
import com.microservices.pps.event.CartEvent;
import com.microservices.pps.repository.ProductRepository;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class KafkaConsumerService {

    private final ProductRepository productRepository;

    public KafkaConsumerService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @KafkaListener(topics = "cart-topic", groupId = "inventory-group")
    public void consume(CartEvent event) {

        System.out.println("📥 Received event: " + event);

        Product product = productRepository.findById(event.getProductId())
                .orElseThrow(() -> new RuntimeException("Product not found"));

        // ✅ Reduce stock
        product.setStock(product.getStock() - event.getQuantity());

        productRepository.save(product);

        System.out.println("✅ Stock updated: " + product.getId());
    }
}
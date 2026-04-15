package com.microservices.cartservice.kafka;

import com.microservices.cartservice.event.CartEvent;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class KafkaProducerService {

    // ✅ Kafka topic name
    private static final String TOPIC = "cart-topic";

    private final KafkaTemplate<String, CartEvent> kafkaTemplate;

    // ✅ Constructor Injection
    public KafkaProducerService(KafkaTemplate<String, CartEvent> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    // ✅ Method to send event
    public void sendEvent(CartEvent event) {

        kafkaTemplate.send(TOPIC, event);

        System.out.println("✅ Sent to Kafka: " +
                "cartId=" + event.getCartId() +
                ", productId=" + event.getProductId() +
                ", quantity=" + event.getQuantity());
    }
}
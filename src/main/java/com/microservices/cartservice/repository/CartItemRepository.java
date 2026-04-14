package com.microservices.cartservice.repository;

import com.microservices.cartservice.entity.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.List;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {

    // 🔥 Fetch all items for a specific cart
    List<CartItem> findByCart_Id(Long cartId);
    @Query(value = "SELECT * FROM cart_item WHERE cart_id = :cartId", nativeQuery = true)
    List<CartItem> findByCartId(Long cartId);



}



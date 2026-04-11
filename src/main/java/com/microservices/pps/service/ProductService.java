package com.microservices.pps.service;

import com.microservices.pps.entity.Product;
import com.microservices.pps.repository.ProductRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductService {

    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    // ✅ Create Product
    public Product createProduct(Product product) {
        return productRepository.save(product);
    }

    // ✅ Get All Products
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    // ✅ Get Product By ID
    public Optional<Product> getProductById(Long id) {
        return productRepository.findById(id);
    }

    // ✅ Validate Stock
    public boolean isStockAvailable(Long productId, int quantity) {
        Optional<Product> product = productRepository.findById(productId);

        if (product.isPresent()) {
            return product.get().getStock() >= quantity;
        }
        return false;
    }
}
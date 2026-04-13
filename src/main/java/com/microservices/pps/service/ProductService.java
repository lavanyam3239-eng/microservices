package com.microservices.pps.service;

import com.microservices.pps.entity.Product;
import com.microservices.pps.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import org.springframework.data.domain.*;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;

    // CREATE
    public Product saveProduct(Product product) {
        return productRepository.save(product);
    }

    // GET ALL
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    // GET BY ID
    public Product getProductById(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found"));
    }

    // UPDATE
    public Product updateProduct(Long id, Product product) {
        Product existing = getProductById(id);
        existing.setName(product.getName());
        existing.setPrice(product.getPrice());
        existing.setStock(product.getStock());
        return productRepository.save(existing);
    }

    // DELETE
    public void deleteProduct(Long id) {
        productRepository.deleteById(id);
    }

    public Page<Product> getProducts(int page, int size, String sortBy) {

        Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy));

        return productRepository.findAll(pageable);
    }
    // ============================
// FILTER PRODUCTS
// ============================
    public List<Product> filterProducts(double price) {
        return productRepository.findAll()
                .stream()
                .filter(p -> p.getPrice() > price)
                .toList();
    }

    // ============================
// GET PRODUCT NAMES
// ============================
    public List<String> getProductNames() {
        return productRepository.findAll()
                .stream()
                .map(Product::getName)
                .toList();
    }

    // ✅ Get products above price
    public List<Product> getProductsAbovePrice(double price) {
        return productRepository.findProductsAbovePrice(price);
    }
}
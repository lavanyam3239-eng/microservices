package com.microservices.pps.service;

import com.microservices.pps.entity.Product;
import com.microservices.pps.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.data.domain.*;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;

    // ============================
    // CREATE
    // ============================
    public Product saveProduct(Product product) {
        return productRepository.save(product);
    }

    // ============================
    // GET ALL
    // ============================
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    // ============================
    // GET BY ID
    // ============================
    public Product getProductById(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found"));
    }

    // ============================
    // UPDATE
    // ============================
    public Product updateProduct(Long id, Product product) {
        Product existing = getProductById(id);
        existing.setName(product.getName());
        existing.setPrice(product.getPrice());
        existing.setStock(product.getStock());
        return productRepository.save(existing);
    }

    // ============================
    // DELETE
    // ============================
    public void deleteProduct(Long id) {
        productRepository.deleteById(id);
    }

    // ============================
    // 🔥 PAGINATION + SORTING + REMOVE DUPLICATES
    // ============================
    public Page<Product> getProducts(int page, int size, String sortBy) {

        if (sortBy == null || sortBy.isEmpty()) {
            sortBy = "id";
        }

        Pageable pageable = PageRequest.of(
                page,
                size,
                Sort.by(Sort.Direction.ASC, sortBy)
        );

        Page<Product> productPage = productRepository.findAll(pageable);

        // 🔥 REMOVE DUPLICATES BASED ON NAME
        Map<String, Product> uniqueMap = productPage.getContent()
                .stream()
                .collect(Collectors.toMap(
                        Product::getName,
                        p -> p,
                        (existing, duplicate) -> existing // keep first
                ));

        List<Product> uniqueProducts = uniqueMap.values()
                .stream()
                .toList();

        return new PageImpl<>(
                uniqueProducts,
                pageable,
                uniqueProducts.size()
        );
    }

    // ============================
    // 🔥 ADVANCED SORTING
    // ============================
    public Page<Product> getProducts(int page, int size, String sortBy, String direction) {

        if (sortBy == null || sortBy.isEmpty()) {
            sortBy = "id";
        }

        Sort sort = direction.equalsIgnoreCase("desc")
                ? Sort.by(sortBy).descending()
                : Sort.by(sortBy).ascending();

        Pageable pageable = PageRequest.of(page, size, sort);

        Page<Product> productPage = productRepository.findAll(pageable);

        // 🔥 REMOVE DUPLICATES
        Map<String, Product> uniqueMap = productPage.getContent()
                .stream()
                .collect(Collectors.toMap(
                        Product::getName,
                        p -> p,
                        (existing, duplicate) -> existing
                ));

        List<Product> uniqueProducts = uniqueMap.values()
                .stream()
                .toList();

        return new PageImpl<>(
                uniqueProducts,
                pageable,
                uniqueProducts.size()
        );
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

    // ============================
    // CUSTOM QUERY
    // ============================
    public List<Product> getProductsAbovePrice(double price) {
        return productRepository.findProductsAbovePrice(price);
    }
}
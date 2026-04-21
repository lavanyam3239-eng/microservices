package com.microservices.pps.service;

import com.microservices.pps.entity.Product;
import com.microservices.pps.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
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
    // 🔥 PAGINATION + SORTING + REMOVE DUPLICATES (FINAL FIX)
    // ============================
    public Page<Product> getProducts(int page, int size, String sortBy) {

        if (sortBy == null || sortBy.isEmpty()) {
            sortBy = "id";
        }

        // 🔥 Step 1: Fetch ALL sorted data
        List<Product> allProducts = productRepository.findAll(
                Sort.by(Sort.Direction.ASC, sortBy)
        );

        // 🔥 Step 2: Remove duplicates (preserve order)
        List<Product> uniqueProducts = allProducts.stream()
                .collect(Collectors.toMap(
                        Product::getName,
                        p -> p,
                        (existing, duplicate) -> existing,
                        java.util.LinkedHashMap::new
                ))
                .values()
                .stream()
                .toList();

        // 🔥 Step 3: Manual pagination
        int start = page * size;
        int end = Math.min(start + size, uniqueProducts.size());

        List<Product> paginatedList = uniqueProducts.subList(start, end);

        // 🔥 Step 4: Return Page
        return new PageImpl<>(
                paginatedList,
                PageRequest.of(page, size),
                uniqueProducts.size()
        );
    }

    // ============================
    // 🔥 ADVANCED SORTING + REMOVE DUPLICATES
    // ============================
    public Page<Product> getProducts(int page, int size, String sortBy, String direction) {

        if (sortBy == null || sortBy.isEmpty()) {
            sortBy = "id";
        }

        Sort sort = direction.equalsIgnoreCase("desc")
                ? Sort.by(sortBy).descending()
                : Sort.by(sortBy).ascending();

        // 🔥 Step 1: Fetch ALL sorted data
        List<Product> allProducts = productRepository.findAll(sort);

        // 🔥 Step 2: Remove duplicates
        List<Product> uniqueProducts = allProducts.stream()
                .collect(Collectors.toMap(
                        Product::getName,
                        p -> p,
                        (existing, duplicate) -> existing,
                        java.util.LinkedHashMap::new
                ))
                .values()
                .stream()
                .toList();

        // 🔥 Step 3: Manual pagination
        int start = page * size;
        int end = Math.min(start + size, uniqueProducts.size());

        List<Product> paginatedList = uniqueProducts.subList(start, end);

        return new PageImpl<>(
                paginatedList,
                PageRequest.of(page, size),
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
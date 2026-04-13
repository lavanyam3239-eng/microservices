package com.microservices.pps.controller;

import com.microservices.pps.entity.Product;
import com.microservices.pps.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/products")
public class ProductController {

    @Autowired
    private ProductService productService;

    // ===============================
    // CREATE PRODUCT
    // ===============================
    @PostMapping
    public Product createProduct(@RequestBody Product product) {
        return productService.saveProduct(product);
    }

    // ===============================
    // GET ALL PRODUCTS
    // ===============================
    @GetMapping
    public List<Product> getAllProducts() {
        return productService.getAllProducts();
    }

    // ===============================
    // GET PRODUCT BY ID (FIXED PATH)
    // ===============================
    @GetMapping("/id/{id}")   // 🔥 IMPORTANT CHANGE
    public Product getProductById(@PathVariable Long id) {
        return productService.getProductById(id);
    }

    // ===============================
    // PAGINATION + SORTING
    // ===============================
    @GetMapping("/page")
    public Page<Product> getProducts(
            @RequestParam int page,
            @RequestParam int size,
            @RequestParam String sortBy) {

        return productService.getProducts(page, size, sortBy);
    }

    // ===============================
    // FILTER USING STREAMS
    // ===============================
    @GetMapping("/filter")
    public List<Product> filterProducts(@RequestParam double price) {
        return productService.filterProducts(price);
    }

    // ===============================
    // GET PRODUCT NAMES (STREAMS)
    // ===============================
    @GetMapping("/names")
    public List<String> getProductNames() {
        return productService.getProductNames();
    }
    // ✅ API: Get products above certain price
    @GetMapping("/above-price/{price}")
    public List<Product> getProductsAbovePrice(@PathVariable double price) {
        return productService.getProductsAbovePrice(price);
    }
}
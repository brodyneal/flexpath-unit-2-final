package org.example.controllers;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.bind.annotation.RequestMapping;


import org.example.daos.ProductDao;
import org.example.models.Product;

import java.util.List;

@RequestMapping("/api")
@RestController
public class ProductController {

    private final ProductDao productDao;

    public ProductController(ProductDao productDao) {
        this.productDao = productDao;
    }

    // GET all products
    @GetMapping("/products")
    public List<Product> getProducts() {
        return productDao.getProducts();
    }

    // GET product by ID
    @GetMapping("/products/{id}")
    public Product getProductById(@PathVariable("id") int id) {
        Product product = productDao.getProductById(id);

        if (product == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }

        return product;
    }

    // CREATE product
    @PostMapping("/products")
    public ResponseEntity<Product> createProduct(@RequestBody Product product) {
        Product createdProduct = productDao.createProduct(product);

        return ResponseEntity.status(HttpStatus.CREATED).body(createdProduct);
    }

    // UPDATE product
    @PutMapping("/products/{id}")
    public Product updateProduct(
            @PathVariable("id") int id,
            @RequestBody Product product) {

        Product updatedProduct = productDao.updateProduct(product, id);

        if (updatedProduct == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }

        return updatedProduct;
    }

    // DELETE product
    @DeleteMapping("/products/{id}")
    public int deleteProduct(@PathVariable("id") int id) {
        int rowsAffected = productDao.deleteProduct(id);

        if (rowsAffected == 0) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }

        return rowsAffected;
    }
}
package com.example.crudApp.controller;

import com.example.crudApp.model.Product;
import com.example.crudApp.service.ProductService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "http://localhost:5173")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }
    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    @GetMapping("/products")
    public ResponseEntity<List<Product>> getProducts() {
        return new ResponseEntity<>(productService.getProducts(), HttpStatus.OK);
    }

    @GetMapping("/product/{id}")
    public ResponseEntity<Optional<Product>> getProductById(@PathVariable int id) {
        return new ResponseEntity<>(productService.getProductById(id), HttpStatus.OK);
    }

    @GetMapping("/product/{id}/image")
    public ResponseEntity<byte[]> getImageById(@PathVariable int id) {

        Optional<Product> product = productService.getProductById(id);

        if (product.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok()
                .contentType(MediaType.valueOf(product.get().getImageType()))
                .body(product.get().getImageData());
    }

    @PostMapping("/product")
    public ResponseEntity<?> addProduct(
            @RequestPart("product") String productJson,
            @RequestPart("imageFile") MultipartFile imageFile) {

        try {
            ObjectMapper mapper = new ObjectMapper();
            mapper.findAndRegisterModules();

            Product product = mapper.readValue(productJson, Product.class);

            Product saved = productService.addProduct(product, imageFile);

            return ResponseEntity.ok(saved);

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body(e.toString());
        }
    }

    @PutMapping("/products")
    public ResponseEntity<?> updateProduct(
            @RequestPart Product product,
            @RequestPart MultipartFile imageFile) {

        try {
            Product productRes = productService.updateProduct(product, imageFile);
            return new ResponseEntity<>(productRes, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/products/{id}")
    public void deleteProduct(@PathVariable int id) {
        productService.deleteProduct(id);
    }

    @GetMapping("/products/search")
    public ResponseEntity<List<Product>> searchProducts(@RequestParam String name) {
        List<Product> products = productService.searchProducts(name);
        return new ResponseEntity<>(products, HttpStatus.OK);
    }

}
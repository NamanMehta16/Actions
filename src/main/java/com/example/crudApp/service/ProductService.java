package com.example.crudApp.service;
import com.example.crudApp.model.Product;
import com.example.crudApp.repository.ProductRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service
public class ProductService {
    @Autowired
    ProductRepo repo;
    List<Product> products;
    public List<Product> getProducts(){
        return repo.findAll();
    }
    public Optional<Product> getProductById(int id)
    {
        return repo.findById(id);
    }
    public Product addProduct(Product product, MultipartFile imageFile) throws IOException {
        product.setName(imageFile.getOriginalFilename());
        product.setImageType(imageFile.getContentType());
        product.setImageData(imageFile.getBytes());
        return repo.save(product);
    }
    public Product updateProduct(Product product, MultipartFile imageFile) throws IOException {
        product.setName(imageFile.getOriginalFilename());
        product.setImageType(imageFile.getContentType());
        product.setImageData(imageFile.getBytes());
        return repo.save(product);
    }
    public void deleteProduct(int id)
    {
        repo.deleteById(id);
    }
    public List<Product> searchProducts(String name)
    {
        return repo.findByNameContainingIgnoreCase(name);
    }
}

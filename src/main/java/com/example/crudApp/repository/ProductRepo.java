package com.example.crudApp.repository;

import com.example.crudApp.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepo extends JpaRepository<Product,Integer> {
    List<Product> findByNameContainingIgnoreCase(String keyword);

}

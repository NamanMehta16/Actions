package com.example.crudApp.repository;

import com.example.crudApp.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepo extends JpaRepository<User,Integer> {
    User findByName(String name);
}

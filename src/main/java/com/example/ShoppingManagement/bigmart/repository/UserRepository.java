package com.example.ShoppingManagement.bigmart.repository;

import com.example.ShoppingManagement.bigmart.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
}

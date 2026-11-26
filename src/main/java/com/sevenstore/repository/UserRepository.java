package com.sevenstore.repository;

import com.sevenstore.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    boolean existsByApiKey(String apiKey);
}

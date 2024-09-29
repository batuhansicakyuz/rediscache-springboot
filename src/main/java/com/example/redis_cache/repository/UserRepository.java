package com.example.redis_cache.repository;

import com.example.redis_cache.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}

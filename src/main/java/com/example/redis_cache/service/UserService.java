package com.example.redis_cache.service;

import com.example.redis_cache.dto.CreateUserDto;
import com.example.redis_cache.dto.UpdateUserDto;
import com.example.redis_cache.model.User;
import com.example.redis_cache.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    @CacheEvict(value = {"users","user_id"}, allEntries = true)
    public User createUser(CreateUserDto dto) {
        var user = userRepository.save(dto.toEntity(dto));
        return user;
    }

    @Cacheable(value = "users", key = "#root.methodName", unless = "#result == null")
    public List<User> getUsers() {
        return  userRepository.findAll();
    }

    @Cacheable(cacheNames = "user_id", key = "#root.methodName + #id", unless = "#result == null")
    public User getUserById(Long id) {
        return userRepository.findById(id).orElse(null);
    }

    @CacheEvict(value = {"users", "user_id"}, allEntries = true)
    public String deleteUser(Long id) {
        userRepository.deleteById(id);
        return "User deleted";
    }

    @CachePut(cacheNames = "user_id", key = "'getUserById' + #dto.id",
            unless = "#result == null")
    public User updateUser(UpdateUserDto dto) {
        Optional<User> updateUser = userRepository.findById(dto.getId());
        if(updateUser.isPresent()){
             User user1 = updateUser.get();
             user1.setPassword(dto.getPassword());
             return userRepository.save(user1);
        } else {
            return null;
        }
    }
}

package com.example.demo.user.repository;

import com.example.demo.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {



    Optional<User> findByUserEmail(String email);
    Optional<User> findByRefreshToken(String refreshToken);
}

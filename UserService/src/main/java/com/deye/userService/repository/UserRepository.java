package com.deye.userService.repository;

import com.deye.userService.domain.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Integer> {
    boolean existsByUserName(String userName);

    User findByEmail(String email);
    boolean existsByEmail(String email);
}

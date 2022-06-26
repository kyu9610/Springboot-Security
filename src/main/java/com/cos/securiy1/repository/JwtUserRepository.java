package com.cos.securiy1.repository;

import com.cos.securiy1.model.JwtUser;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JwtUserRepository extends JpaRepository<JwtUser,Long> {
    public JwtUser findByUsername(String username);
}

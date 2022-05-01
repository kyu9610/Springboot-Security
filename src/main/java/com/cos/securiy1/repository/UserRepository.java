package com.cos.securiy1.repository;

import com.cos.securiy1.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

// CRUD 함수를 들고있음
// @Repository 어노테이션이 없어도 IOC가 된다.
public interface UserRepository extends JpaRepository<User,Integer> {
    // findBy규칙 -> Username 문법
    // select * from user where username = ?
    public User findByUsername(String username);
}

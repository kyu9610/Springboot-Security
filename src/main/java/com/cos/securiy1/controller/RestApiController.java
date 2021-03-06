package com.cos.securiy1.controller;

import com.cos.securiy1.model.JwtUser;
import com.cos.securiy1.repository.JwtUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class RestApiController {

    @Autowired
    private JwtUserRepository jwtUserRepository;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @GetMapping("home")
    public String home(){
        return "<h1>home</h1>";
    }

    @PostMapping("token")
    public String token(){
        return "<h1>token</h1>";
    }

    @PostMapping("join")
    public String join(@RequestBody JwtUser jwtUser){
        jwtUser.setPassword(bCryptPasswordEncoder.encode(jwtUser.getPassword()));
        jwtUser.setRoles("ROLE_USER");
        jwtUserRepository.save(jwtUser);
        return "회원가입완료";
    }

    // user,manager,admin 권한만 접근 가능
    @GetMapping("/api/v1/user")
    public String user(){
        return "user";
    }

    // manager,admin 권한만 접근 가능
    @GetMapping("/api/v1/manager")
    public String manager(){
        return "manager";
    }

    // admin 권한만 접근 가능
    @GetMapping("/api/v1/admin")
    public String admin(){
        return "admin";
    }
}

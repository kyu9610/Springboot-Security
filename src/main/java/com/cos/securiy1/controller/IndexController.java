package com.cos.securiy1.controller;

import com.cos.securiy1.auth.PrincipalDetails;
import com.cos.securiy1.model.User;
import com.cos.securiy1.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller // View Return
public class IndexController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @GetMapping("/test/login")
    public @ResponseBody String loginTest(Authentication authentication, @AuthenticationPrincipal PrincipalDetails userDetails){
        System.out.println("/test/login =============");
        PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal();
        System.out.println("authentication : " + authentication.getPrincipal());

        System.out.println("userDetails : " + userDetails.getUser());
        return "세션 정보 확인";
    }

    @GetMapping("/test/oauth/login")
    public @ResponseBody String testOAuthLogin(Authentication authentication, @AuthenticationPrincipal OAuth2User oauth){
        System.out.println("/test/oauth/login =============");
        OAuth2User oauth2User = (OAuth2User) authentication.getPrincipal();
        System.out.println("authentication : " + oauth2User.getAttributes());

        System.out.println("oauth2User : " + oauth.getAttributes());
        return "OAuth 세션 정보 확인";
    }

    @GetMapping({"","/"})
    public String index(){
        // 머스테치 기본폴더 src/main/resources/
        // ViewResolver 설정 : templates (prefix) , mustache(suffix) - 생략 가능.
        return "index"; // src/main/resources/templates/index.mustache 를 찾게된다.
    }

    // OAuth, 일반 로그인 둘다 PrincipalDetails로 받을 수 있다.
    @GetMapping("/user")
    public @ResponseBody String user(@AuthenticationPrincipal PrincipalDetails principalDetails){
        System.out.println("principalDetails : " + principalDetails.getUser());
        return "user";
    }

    @GetMapping("/admin")
    public @ResponseBody String admin(){
        return "admin";
    }

    @GetMapping("/manager")
    public @ResponseBody String manager(){
        return "manager";
    }

    // 스프링 시큐리티가 해당 주소를 낚아챈다.
    @GetMapping("/loginForm")
    public String loginForm(){
        return "loginForm";
    }

    @GetMapping("/joinForm")
    public String joinForm(){
        return "joinForm";
    }

    @PostMapping("/join")
    public String join(User user){
        user.setRole("ROLE_USER");
        String rawPassword = user.getPassword();
        String encPassword = bCryptPasswordEncoder.encode(rawPassword);
        user.setPassword(encPassword);
        userRepository.save(user); // 회원가입이 잘 됨. 비밀번호 => 1234 => 시큐리티로 로그인이 되지 않는다. 패스워드 암호화필요
        return "redirect:/loginForm";
    }

    @GetMapping("/joinProc")
    public @ResponseBody String joinProc(){
        return "redirect:/loginForm";
    }

    @Secured("ROLE_ADMIN") // 간단하게 걸고싶을때 (하나만 걸고싶을때)
    @GetMapping("/info")
    public @ResponseBody String info(){
        return "개인정보";
    }

    //@PostAuthorzie , 메소드 실행 후에
    @PreAuthorize("hasRole('ROLE_MANAGER') or hasRole('ROLE_ADMIN')") // data 메소드 실행 직전에
    @GetMapping("/data")
    public @ResponseBody String data(){
        return "데이터정보";
    }
}

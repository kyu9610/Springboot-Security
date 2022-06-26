package com.cos.securiy1.jwt;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

// Spring Security에 있는 필터를 (/login 요청시 Post로 ID,PWD 전송시 동작)
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
    private final AuthenticationManager authenticationManager;

    // (/login 요청시 로그인 시도를 위해 실행하는 함수)
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {

        // 1. username,password 를 받아서

        // 2. 정상인지 로그인을 시도해본다. authenticationManager로 로그인 시도시
        // PrincipalDetailsService 호출 -> loadUserByUsername() 함수 실행

        // 3. PrincipalDetails 를 세션에 담고 (권한 관리를 위해서)

        // 4. JWT토큰을 만들어줘서 인증시도

        return super.attemptAuthentication(request, response);
    }
}

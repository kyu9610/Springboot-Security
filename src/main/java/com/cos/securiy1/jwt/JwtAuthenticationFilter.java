package com.cos.securiy1.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.cos.securiy1.auth.PrincipalDetails;
import com.cos.securiy1.model.JwtUser;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;

// Spring Security에 있는 필터를 (/login 요청시 Post로 ID,PWD 전송시 동작)
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
    private final AuthenticationManager authenticationManager;

    // (/login 요청시 로그인 시도를 위해 실행하는 함수)
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {

        // 1. username,password 를 받아서
        try {
            ObjectMapper om = new ObjectMapper();
            JwtUser jwtUser = om.readValue(request.getInputStream(),JwtUser.class);
            System.out.println(jwtUser);

            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(jwtUser.getUsername(),jwtUser.getPassword());
            // PrincipalDetailsService의 loadUserByUsername() 함수가 실행됨
            Authentication authentication = authenticationManager.authenticate(authenticationToken);

            // authentication 객체가 session 영역에 저장됨 => 로그인이 되었다는 뜻.
            //PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal();
            //System.out.println("로그인 완료됨 : " + principalDetails.getUser().getUsername());

            return authentication;
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }


    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
        System.out.println("인증 완료");
        PrincipalDetails principalDetails = (PrincipalDetails) authResult.getPrincipal();

        // HASH 암호방식으로 jwt토큰을 생성한다.
        String jwtToken = JWT.create()
                .withSubject("cos토큰")
                .withExpiresAt(new Date(System.currentTimeMillis() + (60000 * 10)))
                .withClaim("id",principalDetails.getUser().getId()) // 비공개 claim (내가 넣고 싶은)
                .withClaim("username",principalDetails.getUser().getUsername())
                .sign(Algorithm.HMAC512("cos")); // 서버만 알고있는 secret key


        response.addHeader("Authorization","Bearer " + jwtToken);
        super.successfulAuthentication(request, response, chain, authResult);
    }
}

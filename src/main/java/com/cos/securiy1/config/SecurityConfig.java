package com.cos.securiy1.config;

import com.cos.securiy1.oauth.PrincipalOauth2UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration
@EnableWebSecurity // 활성화 , 스프링 시큐리티 필터가 스프링 필터 체인에 등록
@EnableGlobalMethodSecurity(securedEnabled = true, prePostEnabled = true) // secured 어노테이션 활성화, preAuthorize/postAuthorize 어노테이션 활성화
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private PrincipalOauth2UserService principalOauth2UserService;

    // 해당 메서드의 리턴되는 오브젝트를 IoC로 등록해준다.
    @Bean
    public BCryptPasswordEncoder encoedePwd(){
        return new BCryptPasswordEncoder();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable(); // 비활성화
        http.authorizeRequests()
                .antMatchers("/user/**").authenticated() // 다음과 같은 주소는 인증이 필요
                .antMatchers("/manager/**").access("hasRole('ROLE_ADMIN') or hasRole('ROLE_MANAGER')")
                .antMatchers("/admin/**").access("hasRole('ROLE_ADMIN')") // 인증뿐 아니라 권한이 있어야함
                .anyRequest().permitAll() // 다른 것은 모두 허용
                .and()
                .formLogin()
                .loginPage("/loginForm") // 로그인 페이지 설정
                .loginProcessingUrl("/login") // login 주소가 호출되면 시큐리티가 로그인을 진행해 준다.
                .defaultSuccessUrl("/") // 완료시 메인 페이지로 이동
                .and()
                .oauth2Login()
                .loginPage("/loginForm") // 구글 로그인 완료까지, 이후 후처리가 필요
                // 1. 코드받기(인증) 2. access Token(권한), 3.사용자 프로필 정보를 가져오기 4.그 정보를 토대로 회원가입 자동진행
                // if) 추가적인 고객정보가 필요하게되면 추가적인 회원가입 view가 필요하다.
                // 구글 로그인시 -> access Token + 사용자 프로필을 한번에 받음.
                .userInfoEndpoint()
                .userService(principalOauth2UserService);
    }
}

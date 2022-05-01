package com.cos.securiy1.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration
@EnableWebSecurity // 활성화 , 스프링 시큐리티 필터가 스프링 필터 체인에 등록
public class SecurityConfig extends WebSecurityConfigurerAdapter {

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
                .defaultSuccessUrl("/"); // 완료시 메인 페이지로 이동
    }
}

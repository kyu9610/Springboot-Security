package com.cos.securiy1.config;

import com.cos.securiy1.filter.MyFilter1;
import com.cos.securiy1.oauth.PrincipalOauth2UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.web.filter.CorsFilter;

@Configuration
@EnableWebSecurity // 활성화 , 스프링 시큐리티 필터가 스프링 필터 체인에 등록
@RequiredArgsConstructor
@EnableGlobalMethodSecurity(securedEnabled = true, prePostEnabled = true) // secured 어노테이션 활성화, preAuthorize/postAuthorize 어노테이션 활성화
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private PrincipalOauth2UserService principalOauth2UserService;

    private final CorsFilter corsFilter;

    // 해당 메서드의 리턴되는 오브젝트를 IoC로 등록해준다.
    @Bean
    public BCryptPasswordEncoder encoedePwd(){
        return new BCryptPasswordEncoder();
    }

        @Override
        protected void configure(HttpSecurity http) throws Exception {
            http.csrf().disable(); // 비활성화
            http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS) // 세션 사용x
            .and()
                    .addFilter(corsFilter) // @CrossOrigin(인증x), 시큐리티 필터에 등록을 해야 인증있을때 사용
                    .formLogin().disable()
                    .httpBasic().disable() // ID + PWD 를 들고가는 방식인 httpBasic이 아닌 Token을 들고가는 Bearer 방식을 사용하기위해
                    .authorizeRequests()
                    .antMatchers("/api/v1/user/**")
                    .access("hasRole('ROLE_USER') or hasRole('ROLE_MANAGER') or hasRole('ROLE_ADMIN')")
                    .antMatchers("/api/v1/manager/**")
                    .access("hasRole('ROLE_MANAGER') or hasRole('ROLE_ADMIN')")
                    .antMatchers("/api/v1/admin/**")
                    .access("hasRole('ROLE_ADMIN')")
                    .anyRequest().permitAll(); // formLonin 사용안한다.
//            http.authorizeRequests()
//                    .antMatchers("/user/**").authenticated() // 다음과 같은 주소는 인증이 필요
//                    .antMatchers("/manager/**").access("hasRole('ROLE_ADMIN') or hasRole('ROLE_MANAGER')")
//                    .antMatchers("/admin/**").access("hasRole('ROLE_ADMIN')") // 인증뿐 아니라 권한이 있어야함
//                    .anyRequest().permitAll() // 다른 것은 모두 허용
//                    .and()
//                    .formLogin()
//                    .loginPage("/loginForm") // 로그인 페이지 설정
//                    .loginProcessingUrl("/login") // login 주소가 호출되면 시큐리티가 로그인을 진행해 준다.
//                    .defaultSuccessUrl("/") // 완료시 메인 페이지로 이동
//                    .and()
//                    .oauth2Login()
//                    .loginPage("/loginForm") // 구글 로그인 완료까지, 이후 후처리가 필요
//                    // 1. 코드받기(인증) 2. access Token(권한), 3.사용자 프로필 정보를 가져오기 4.그 정보를 토대로 회원가입 자동진행
//                    // if) 추가적인 고객정보가 필요하게되면 추가적인 회원가입 view가 필요하다.
//                    // 구글 로그인시 -> access Token + 사용자 프로필을 한번에 받음.
//                    .userInfoEndpoint()
//                    .userService(principalOauth2UserService);
        }
}

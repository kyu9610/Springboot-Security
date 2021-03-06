package com.cos.securiy1.auth;

// security가 /login 주소 요청을 낚아채서 로그인을 진행
// 로그인이 진행이 완료되면 security session을 만들어 준다. ( Security ContextHolder)
// 오브젝트 => Authentication 타입의 객체
// Authentication 안에 User 정보가 있어야 됨.
// User 오브젝트 타입 => UserDetails 타입 객체

// Security Session => Authentication => UserDetails(PrincipalDetails)

import com.cos.securiy1.model.JwtUser;
import com.cos.securiy1.model.User;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

@Data
public class PrincipalDetails implements UserDetails, OAuth2User {

    private User user; // 컴포지션
    private JwtUser jwtUser;
    private Map<String,Object> attributes;

    // 일반 로그인 생성자
    public PrincipalDetails(User user){
        this.user = user;
    }

    public PrincipalDetails(JwtUser jwtUser){
        this.jwtUser = jwtUser;
    }

    // OAuth 로그인 생성자
    public PrincipalDetails(User user,Map<String,Object> attributes){
        this.user = user;
        this.attributes = attributes;
    }

    public JwtUser getUser(){
        return jwtUser;
    }

    @Override
    public Map<String, Object> getAttributes() {
        return attributes;
    }

    // 해당 User의 권한을 리턴하는곳.
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
//        Collection<GrantedAuthority> collect = new ArrayList<>();
//        collect.add(new GrantedAuthority() {
//            @Override
//            public String getAuthority() {
//                return user.getRole();
//            }
//        });
//        return collect;
        Collection<GrantedAuthority> authorities = new ArrayList<>();
        jwtUser.getRoleList().forEach(r->{
            authorities.add(()->r);
        });
        return authorities;
    }


    // User 의 password 리턴
    @Override
    public String getPassword() {
        //return user.getPassword();
        return jwtUser.getPassword();
    }

    @Override
    public String getUsername() {
        //return user.getUsername();
        return jwtUser.getUsername();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {

        // 사이트 내에서 1년동안 로그인을 안하면 휴먼계정을 전환을 하도록 하겠다.
        // -> loginDate 타입을 모아놨다가 이 값을 false로 return 해버리면 된다.
        return true;
    }

    @Override
    public String getName() {
        return null;
    }
}

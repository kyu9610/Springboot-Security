package com.cos.securiy1.auth;

import com.cos.securiy1.model.User;
import com.cos.securiy1.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

// Security 설정에서 loginProcessUrl("/login")으로 걸어뒀기 때문에 /login 요청이오면 자동으로 타입이 IoC 되어있는
// loadUserByUsername 함수가 수행 => "약속"
// 함수 종료시 @AuthenticationPrncipal 어노테이션이 만들어진다.
@Service
public class PrincipalDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    // 시큐리티 session(내부 Authentication(내부 UserDetails))
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User userEntity = userRepository.findByUsername(username);
        if(userEntity != null){
            return new PrincipalDetails(userEntity);
        }
        return null;
    }
}

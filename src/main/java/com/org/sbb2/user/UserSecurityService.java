package com.org.sbb2.user;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class UserSecurityService implements UserDetailsService {
    // 스프링 시큐리티가 제공하는 UserDetailsService 구현(implements)

    private final UserRepository userRepository;

    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // loadUserByUsername 메서드: 사용자명(username)으로 스프링 시큐리티의 사용자(User)객체를 조회하여 리턴하는 메서드
        // 사용자명으로 SiteUser객체 조회. -> 만약 사용자명에 해당하는 데이터 없을 경우 UsernameNotFoundException 예외 발생
        // 사용자명이 "admin"인 경우 -> ADMIN권한 부여. 그 이외에는 USER권한 부여 -> User객체 생성해 반환. -> 객체는 시큐리티에서 사용
        // User 생성자에는 사용자명, 비밀번호, 권한 리스트 전달
        // loadUserByUsername 메서드에 의해 리턴된 User 객체의 비밀번호가 사용자로부터 입력받은 비밀번호와 일치하는지 내부적으로 검사

        Optional<SiteUser> _siteUser = this.userRepository.findByUsername(username);
        if (_siteUser.isEmpty()) {
            throw new UsernameNotFoundException("사용자를 찾을 수 없습니다.");
        }
        SiteUser siteUser = _siteUser.get();
        List<GrantedAuthority> authorities = new ArrayList<>();
        if ("admin".equals(username)) {
            authorities.add(new SimpleGrantedAuthority(UserRole.ADMIN.getValue()));
        } else {
            authorities.add(new SimpleGrantedAuthority(UserRole.USER.getValue()));
        }
        return new User(siteUser.getUsername(), siteUser.getPassword(), authorities);
    }
}

package com.org.sbb2;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity //모든 요청 URL이 스프링 시큐리티의 제어를 받도록 만드는 애너테이션. 스프링 시큐리티 활성화 역할
public class SecurityConfig {
    @Bean //스프링에 의해 생성 또는 관리되는 객체.
    SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests((authorizeHttpRequests) -> authorizeHttpRequests
                        .requestMatchers(new AntPathRequestMatcher("/**")).permitAll())
                //인증되지 않은 모든 페이지의 요청을 허락. 로그인하지 않더라고 모든 페이지 접근 가능
                .formLogin((formLogin) -> formLogin
                        // .formLogin: 스프링 시큐리티의 로그인 설정을 담당
                        .loginPage("/user/login")
                        //로그인 페이지의 URL: /user/login
                        .defaultSuccessUrl("/"))
                        //로그인 성공 시 이동할 페이지는 "/"
                .logout((logout) -> logout
                        .logoutRequestMatcher(new AntPathRequestMatcher("/user/logout"))
                        .logoutSuccessUrl("/")
                        .invalidateHttpSession(true)
                        //.invalidateHttpSession(true): 로그아웃 시 생성된 사용자 세션도 삭제하도록 처리
                )
        ;
        return http.build();
    }
    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
    @Bean
    AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        // AuthenticationManager 빈 생성. 스프링 시큐리티의 인증을 처리.
        // AuthenticationManager는 사용자 인증 시 앞에서 작성한 UserSecurityService와 PasswordEncoder를 내부적으로 사용
        // 인증과 권한 부여 프로세스 처리

        return authenticationConfiguration.getAuthenticationManager();
    }
}
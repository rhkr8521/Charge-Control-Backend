package com.ChargeControl.www.Backend.common.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@RequiredArgsConstructor
@EnableWebSecurity
public class SecurityConfig {

    private static final String[] SWAGGER_URL = {
            "/swagger-resources/**",
            "/favicon.ico",
            "/api-docs/**",
            "/swagger-ui/**",
            "/swagger-ui.html",
            "/swagger-ui/index.html",
            "/docs/swagger-ui/index.html",
            "/swagger-ui/swagger-ui.css",
    };

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .headers().frameOptions().disable()
                .and()
                .httpBasic().disable()
                .authorizeRequests(authorize -> authorize
                        .requestMatchers("/h2-console/**").permitAll() // h2-console 경로에 대한 접근을 허용합니다.
                        .requestMatchers("/api/v1/**").permitAll() // h2-console 경로에 대한 접근을 허용합니다.
                        .anyRequest().authenticated()) // 나머지 요청들은 인증을 요구합니다.
                .httpBasic(); // 필요에 따라 다른 인증 방식을 선택할 수 있습니다.

        return http.build();
    }

}

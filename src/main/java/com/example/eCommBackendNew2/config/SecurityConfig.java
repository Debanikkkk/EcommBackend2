package com.example.eCommBackendNew2.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception{
        http
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(
//                                "/swagger-ui/**",
//                                "/swagger-ui.html",
////                                "api/**",
//                                "/v3/api-docs/**",
//                                "/v3/api-docs.yaml"
                                "/api/users/register",
                                "/api/auth/login",
                                "/swagger-ui/**",
                                "/v3/api-docs/**"
                        ).permitAll()
                        .anyRequest().authenticated()
                )
                .csrf(csrf -> csrf.disable()); // fine for now, revisit for stateful/session-based auth later

        return http.build();
    }
}

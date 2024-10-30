package com.projeto.picpay.security.config;

import com.projeto.picpay.security.filter.SecurityFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {
    private static final String WALLET_REQUEST = "/wallets";
    private static final String ROLE_ADMIN = "ADMIN";
    private static final String LOGIN_REQUEST = "/auth/login";
    private static final String SIGNUP_REQUEST = "/auth/signup";

    private final SecurityFilter securityFilter;

    @Bean
    protected SecurityFilterChain filterChain (HttpSecurity http) throws Exception {
        return http
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(s -> s.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(HttpMethod.POST, SIGNUP_REQUEST).permitAll()
                        .requestMatchers(HttpMethod.POST, LOGIN_REQUEST).permitAll()
                        .requestMatchers(HttpMethod.POST, WALLET_REQUEST).hasRole(ROLE_ADMIN)
                        .requestMatchers(HttpMethod.DELETE, WALLET_REQUEST).hasRole(ROLE_ADMIN)
                        .requestMatchers(HttpMethod.PUT, WALLET_REQUEST).hasRole(ROLE_ADMIN)
                        .anyRequest().authenticated())
                .addFilterBefore(securityFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }

    @Bean
    protected AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}

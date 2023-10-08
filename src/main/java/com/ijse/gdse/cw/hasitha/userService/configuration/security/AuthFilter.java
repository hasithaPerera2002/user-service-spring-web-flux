package com.ijse.gdse.cw.hasitha.userService.configuration.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.SecurityWebFiltersOrder;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.authentication.AuthenticationWebFilter;

@EnableWebFluxSecurity
@Configuration
public class AuthFilter {
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityWebFilterChain securityWebFilterChain(
            ServerHttpSecurity http
            , AuthConverter authConverter
            , AuthManager authManager
    ) {
        AuthenticationWebFilter authenticationFilter = new AuthenticationWebFilter(authManager);
        authenticationFilter.setServerAuthenticationConverter(authConverter);
        return http.authorizeExchange(auth -> {
            auth.pathMatchers("/api/v1/user/login").permitAll()
                    .pathMatchers(HttpMethod.POST,"/api/v1/user/register").permitAll()
                    .pathMatchers("/api/v1/admin").hasAnyRole("ADMIN_USER_SERVICE", "ADMIN_VEHICLE_SERVICE")
                    .anyExchange().authenticated();

        })
                .httpBasic().disable()
                .addFilterAt(authenticationFilter, SecurityWebFiltersOrder.AUTHENTICATION)
                .csrf().disable()
                .formLogin().disable().build();

    }
}

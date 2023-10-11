package com.ijse.gdse.cw.hasitha.userService.configuration.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.configurers.SessionManagementConfigurer;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.config.web.server.SecurityWebFiltersOrder;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.authentication.AuthenticationWebFilter;
import org.springframework.web.cors.CorsConfiguration;



import java.util.List;

import static com.ijse.gdse.cw.hasitha.userService.util.enums.RoleType.ADMIN_USER_SERVICE;
import static com.ijse.gdse.cw.hasitha.userService.util.enums.RoleType.ADMIN_VEHICLE_SERVICE;

@EnableWebFluxSecurity
@Configuration
public class AuthFilter  {
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
        CorsConfiguration corsConfiguration = new CorsConfiguration();
        corsConfiguration.setAllowedHeaders(List.of("Authorization", "Cache-Control", "Content-Type"));
        corsConfiguration.setAllowedOrigins(List.of("*"));
        corsConfiguration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        corsConfiguration.setAllowCredentials(false);
        corsConfiguration.setExposedHeaders(List.of("Authorization"));
        SessionManagementConfigurer<HttpSecurity> hSessionManagementConfigurer = new SessionManagementConfigurer<>();
        hSessionManagementConfigurer.sessionCreationPolicy(SessionCreationPolicy.STATELESS);



        return http.authorizeExchange(auth -> {
                    auth.pathMatchers("/api/v1/user/**").permitAll()
                            .pathMatchers(HttpMethod.POST, "/api/v1/user/register").permitAll()
                            .pathMatchers(HttpMethod.PUT, "/api/v1/user/update").permitAll()
                            .pathMatchers(HttpMethod.POST, "/api/v1/admin/login").hasAnyRole(ADMIN_USER_SERVICE.name(), ADMIN_VEHICLE_SERVICE.name())
                            .anyExchange().authenticated();
                })
                .httpBasic().disable()
                .addFilterAt(authenticationFilter, SecurityWebFiltersOrder.AUTHENTICATION)
                .csrf().disable()
                .cors().configurationSource(exchange -> corsConfiguration)
                .and()
                .formLogin().disable().build();

    }


}

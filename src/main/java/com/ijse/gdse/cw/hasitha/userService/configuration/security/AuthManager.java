package com.ijse.gdse.cw.hasitha.userService.configuration.security;

import com.ijse.gdse.cw.hasitha.userService.dto.UserDto;
import com.ijse.gdse.cw.hasitha.userService.service.JWTServices;
import com.ijse.gdse.cw.hasitha.userService.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import reactor.core.publisher.Mono;

@Configuration
@RequiredArgsConstructor
public class AuthManager implements ReactiveAuthenticationManager {

    @Autowired
    final JWTServices jwtServices;

    @Autowired
    final UserService userService;



    @Override
    public Mono<Authentication> authenticate(Authentication authentication) {
        return Mono.justOrEmpty(
                authentication)
                .cast(BearerToken.class)
                .flatMap(bearerToken -> {
                    String email = jwtServices.getEmail(bearerToken.getCredentials());
                    Mono<UserDto> userDto = userService.findByEmail(email).defaultIfEmpty(new UserDto());

                    return userDto.flatMap(user -> {
                        if (user.getEmail() == null) {
                            Mono.error(new IllegalArgumentException("User not found"));
                        }
                       if (jwtServices.validate(bearerToken.getCredentials(), user.getEmail())) {
                           return Mono.justOrEmpty(new UsernamePasswordAuthenticationToken(
                                   user.getEmail(), user.getPassword(), user.getAuthorities()));

                       }
                       Mono.error(new IllegalArgumentException("Invalid / Expired Token"));
                       return Mono.justOrEmpty(new UsernamePasswordAuthenticationToken(
                               user.getEmail(), user.getPassword(), user.getAuthorities()));

                   });
                });

    }
}

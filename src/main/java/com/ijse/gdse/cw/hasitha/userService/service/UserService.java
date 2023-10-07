package com.ijse.gdse.cw.hasitha.userService.service;

import com.ijse.gdse.cw.hasitha.userService.dto.UserDto;

import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface UserService extends ReactiveUserDetailsService {

    Mono<UserDto> saveUser(UserDto userDto);

    Mono<UserDto> getUser(String id);

    Mono<UserDto> updateUser(UserDto userDto);

    void deleteUser(String id);

    Flux<UserDto> getAllUsers();

    Mono<UserDto> findByEmail(String email);
}

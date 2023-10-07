package com.ijse.gdse.cw.hasitha.userService.service.impl;

import com.ijse.gdse.cw.hasitha.userService.dto.UserDto;
import com.ijse.gdse.cw.hasitha.userService.entity.User;
import com.ijse.gdse.cw.hasitha.userService.repo.UserRepo;
import com.ijse.gdse.cw.hasitha.userService.service.UserService;
import com.ijse.gdse.cw.hasitha.userService.validators.UserValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.modelmapper.ModelMapper;
import org.reactivestreams.Publisher;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private final UserValidator userValidator;

    @Autowired
    private  PasswordEncoder passwordEncoder;
    @Override
    public Mono<UserDto> saveUser(UserDto userDto) {
        log.info("user dto : {}",userDto.toString());
        userValidator.validate(userDto);
        //password encryption
        userDto.setPassword(passwordEncoder.encode(userDto.getPassword()));
        return userRepo.insert(modelMapper.map(userDto, User.class))
                .flatMap(savedUser -> {
                    log.info("User saved: {}", savedUser);
                    UserDto savedUserDto = modelMapper.map(savedUser, UserDto.class);
                    savedUserDto.setPassword(null);
                    return Mono.just(savedUserDto);
                });
    }

    @Override
    public Mono<UserDto> getUser(String id) {
        return null;
    }

    @Override
    public Mono<UserDto> updateUser(UserDto userDto) {
        return null;
    }

    @Override
    public void deleteUser(String id) {

    }

    @Override
    public Flux<UserDto> getAllUsers() {
        return null;
    }

    @Override
    public Mono<UserDto> findByEmail(String email) {
        return userRepo.findByEmail(email)
                .flatMap(user -> {
            UserDto userDto = modelMapper.map(user, UserDto.class);
            return Mono.just(userDto);
        });
    }

    @Override
    public Mono<UserDetails> findByUsername(String username) {
        return null;
    }
}

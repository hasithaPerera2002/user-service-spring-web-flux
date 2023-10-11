package com.ijse.gdse.cw.hasitha.userService.api;

import com.ijse.gdse.cw.hasitha.userService.dto.RequestLoginDto;
import com.ijse.gdse.cw.hasitha.userService.dto.UserDto;
import com.ijse.gdse.cw.hasitha.userService.configuration.security.JWTServices;
import com.ijse.gdse.cw.hasitha.userService.service.UserService;
import com.ijse.gdse.cw.hasitha.userService.util.ResponseUtil;
import com.ijse.gdse.cw.hasitha.userService.util.enums.RoleType;
import com.ijse.gdse.cw.hasitha.userService.validators.ObjectValidator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/********************************************************************
 * --
 * Author: hasitha
 * Date: 10/9/2023
 * --
 ********************************************************************/

@RestController
@Slf4j
@RequestMapping("/api/v1/admin")
@PreAuthorize("hasRole('ADMIN_USER_SERVICE')")
public class AdminController {
    private final PasswordEncoder passwordEncoder;
    private final UserService userService;
    private final ObjectValidator<RequestLoginDto> objectValidator;
    private final ObjectValidator<UserDto> userValidator;
    private final JWTServices jwtServices;

    @Autowired
    public AdminController(PasswordEncoder passwordEncoder, UserService userService, ObjectValidator<RequestLoginDto> objectValidator, ObjectValidator<UserDto> userValidator, JWTServices jwtServices) {
        this.passwordEncoder = passwordEncoder;
        this.userService = userService;
        this.objectValidator = objectValidator;
        this.userValidator = userValidator;
        this.jwtServices = jwtServices;
    }

    @PostMapping(value = "/login" ,consumes = "application/json",produces = "application/json")
    public Mono<ResponseEntity<ResponseUtil>> login(@RequestBody RequestLoginDto requestLoginDto) {
        log.info("requestLoginDto : {}", requestLoginDto.getEmail());
        objectValidator.validate(requestLoginDto);
        Mono<UserDto> userDto = userService
                .findByEmail(requestLoginDto
                        .getEmail())
                .switchIfEmpty(Mono.error(new NullPointerException("this Email doesn't exists. Please register before login")));

        log.info("userDto : {}", userDto);


        return userDto.flatMap(userDetails -> {
            if (userDetails.getUserID() != null) {
                if (passwordEncoder.matches(requestLoginDto.getPassword(), userDetails.getPassword())) {
                    return Mono.just(
                            ResponseEntity.ok().body(
                                    new ResponseUtil(200, "Login success",
                                            jwtServices.generate(userDetails.getEmail(),userDetails.getRole())
                                    )
                            )
                    );
                } else {
                    return Mono.just(ResponseEntity
                            .status(HttpStatus.UNAUTHORIZED)
                            .body(new ResponseUtil(401, "Password is incorrect", "")));
                }
            }

            return Mono.just(ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .body(new ResponseUtil(401, "this Email does not exists. Please register before login", "")));
        });

    }

    @PostMapping(value = "/addUser" ,consumes = "application/json",produces = "application/json")
    public Mono<ResponseEntity<ResponseUtil>> addUser(@RequestBody UserDto userDto) {
        userValidator.validate(userDto);
        log.info("userDto : {}", userDto);
        return userService.saveUser(userDto).flatMap(user ->
                        Mono.just(ResponseEntity.ok().body(
                                new ResponseUtil(200, "User saved successfully", user))))
                .onErrorResume(error -> Mono.error(new IllegalArgumentException(error.getMessage())));
    }

    @PutMapping(value = "/updateUser", consumes = "application/json", produces = "application/json")
    public Mono<ResponseEntity<ResponseUtil>> updateUser(@RequestBody UserDto userDto) {
        userValidator.validate(userDto);
        log.info("userDto : {}", userDto);
        return userService.updateUser(userDto).flatMap(user ->
                        Mono.just(ResponseEntity.ok().body(
                                new ResponseUtil(200, "User updated successfully", user))))
                .onErrorResume(error -> Mono.error(new IllegalArgumentException(error.getMessage())));
    }
    @DeleteMapping(value = "/deleteUser", consumes = "application/json")
    public Mono<ResponseEntity<ResponseUtil>> deleteUser(@RequestParam String id) {
        userService.deleteUser(id);
        return Mono.just(ResponseEntity.ok().body(
                new ResponseUtil(200, "User deleted successfully", "")))
                .onErrorResume(error -> Mono.error(new IllegalArgumentException(error.getMessage())));
    }
    @PutMapping(value = "/updateAdmin" ,consumes = "application/json",produces = "application/json")
    public Mono<ResponseEntity<ResponseUtil>> updateAdmin(@RequestBody UserDto userDto) {
        userValidator.validate(userDto);
        log.info("userDto : {}", userDto);
        return userService.updateUser(userDto).flatMap(user ->
                        Mono.just(ResponseEntity.ok().body(
                                new ResponseUtil(200, "Admin updated successfully", user))))
                .onErrorResume(error -> Mono.error(new IllegalArgumentException(error.getMessage())));
    }

    @GetMapping(value = "/getAllUsers",produces = "application/json")
    public Flux<ResponseEntity<ResponseUtil>> getAllUsers() {
        return userService.getAllUsers()
                .map(users -> ResponseEntity.ok().body(
                        new ResponseUtil(200, "Users fetched successfully", users)))
                .onErrorResume(error -> Flux.error(new IllegalArgumentException(error.getMessage())));
    }
    private boolean isAdminRole(RoleType role) {
        return role == RoleType.ADMIN_USER_SERVICE ||
                role == RoleType.ADMIN_GUIDE_SERVICE ||
                role == RoleType.ADMIN_VEHICLE_SERVICE ||
                role == RoleType.ADMIN_HOTEL_SERVICE ||
                role == RoleType.ADMIN_TRAVEL_SERVICE;
    }
}

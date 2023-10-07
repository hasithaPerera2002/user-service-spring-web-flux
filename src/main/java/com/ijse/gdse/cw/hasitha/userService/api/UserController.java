package com.ijse.gdse.cw.hasitha.userService.api;

import com.ijse.gdse.cw.hasitha.userService.dto.RequestLoginDto;
import com.ijse.gdse.cw.hasitha.userService.dto.UserDto;
import com.ijse.gdse.cw.hasitha.userService.service.JWTServices;
import com.ijse.gdse.cw.hasitha.userService.service.UserService;
import com.ijse.gdse.cw.hasitha.userService.util.ResponseUtil;
import lombok.RequiredArgsConstructor;
import org.reactivestreams.Publisher;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/v1/user")
@RequiredArgsConstructor
public class UserController {

    private PasswordEncoder passwordEncoder;
    private UserService userService;

    private final JWTServices jwtServices;

    private final ReactiveUserDetailsService reactiveUserDetailsService;


    @PostMapping
    public Mono<ResponseEntity<ResponseUtil>> saveUser(
            @RequestBody UserDto userDto
    ) {
        return userService.saveUser(userDto).flatMap(user ->
                        Mono.just(ResponseEntity.ok().body(
                                new ResponseUtil(200, "User saved successfully", user))))
                .onErrorResume(error -> Mono.just(ResponseEntity.badRequest().body(
                        new ResponseUtil(400, "Validation Error occurred", error.getMessage()))));

    }

    @GetMapping
    public String getUser() {
        return "hiiiiiiiiiii";
    }

    @PostMapping("/login")
    public Mono<ResponseEntity<ResponseUtil>> login(@RequestBody RequestLoginDto requestLoginDto) {
        Mono<UserDto>userDto = userService.findByEmail(requestLoginDto.getEmail()).defaultIfEmpty(null);
             return userDto.flatMap(userDetails -> {
                    if (userDetails != null) {
                        if (passwordEncoder.matches(requestLoginDto.getPassword(), userDetails.getPassword())) {
                            return Mono.just(
                                    ResponseEntity.ok().body(
                                            new ResponseUtil(200, "Login success",
                                                    jwtServices.generate(userDetails.getEmail())
                                            )
                                    )
                            );
                        }
                    }

                    return Mono.just(ResponseEntity
                            .status(HttpStatus.UNAUTHORIZED)
                            .body(new ResponseUtil(401, "Login failed", "")));
                });

    }

    @GetMapping("/auth")
    public Mono<ResponseEntity<ResponseUtil>> auth() {
        return Mono
                .just(ResponseEntity
                        .ok()
                        .body(new ResponseUtil(200, "Login success", "")));
    }
}

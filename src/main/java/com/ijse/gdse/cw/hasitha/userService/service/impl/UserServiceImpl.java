package com.ijse.gdse.cw.hasitha.userService.service.impl;
import com.ijse.gdse.cw.hasitha.userService.dto.UserDto;
import com.ijse.gdse.cw.hasitha.userService.entity.User;
import com.ijse.gdse.cw.hasitha.userService.repo.UserRepo;
import com.ijse.gdse.cw.hasitha.userService.service.UserService;
import com.ijse.gdse.cw.hasitha.userService.validators.ObjectValidator;
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
    private final UserRepo userRepo;

    @Autowired
    private final ModelMapper modelMapper;

    @Autowired
    private final ObjectValidator <UserDto> objectValidator;

    @Autowired
    private final PasswordEncoder passwordEncoder;
    @Override
    public Mono<UserDto> saveUser(UserDto userDto) {
        log.info("user dto : {}",userDto.toString());
        objectValidator.validate(userDto);
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
        Mono<Boolean> userNotFound = userRepo.existsById(id).switchIfEmpty(Mono.error(new IllegalArgumentException("User not found")));
        return userRepo.findById(id)
                .flatMap(user -> {
                    UserDto userDto = modelMapper.map(user, UserDto.class);
                    return Mono.just(userDto);
                });
    }

    @Override
    public Mono<UserDto> updateUser(UserDto userDto) {
        Mono<User> userMono = userRepo.findById(userDto.getUserID())
                .switchIfEmpty(Mono.error(new NullPointerException("User not found")));
        return userMono.flatMap(user -> {
            user.setUsername(userDto.getUsername());
            user.setContactNumber(userDto.getContactNumber());
            user.setAddress(userDto.getAddress());
            user.setNicNumber(userDto.getNicNumber());
            user.setGender(userDto.getGender());
            user.setRole(userDto.getRole());
            user.setProfilePicture(userDto.getProfilePicture());
            return userRepo.save(user);
        }).flatMap(savedUser -> {
            UserDto savedUserDto = modelMapper.map(savedUser, UserDto.class);
            return Mono.just(savedUserDto);
        });

    }

    @Override
    public void deleteUser(String id) {
            userRepo.deleteById(id).onErrorResume(error -> Mono.error(new IllegalArgumentException(error.getMessage())));
    }

    @Override
    public Flux<UserDto> getAllUsers() {
        return userRepo
                .findAll()
                .map(user -> modelMapper.map(user, UserDto.class));

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
        return userRepo.findByUsername(username)
                .flatMap(user -> {
                    UserDto userDto = modelMapper.map(user, UserDto.class);
                    return Mono.just(userDto);
                });
    }
}

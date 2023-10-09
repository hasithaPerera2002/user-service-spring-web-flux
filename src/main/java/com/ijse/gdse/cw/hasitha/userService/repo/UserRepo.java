package com.ijse.gdse.cw.hasitha.userService.repo;

import com.ijse.gdse.cw.hasitha.userService.entity.User;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;


@Repository
public interface UserRepo extends ReactiveMongoRepository<User, String> {
   Mono<User> findByEmail(String email);

    Mono<Object> findByUsername(String username);
}

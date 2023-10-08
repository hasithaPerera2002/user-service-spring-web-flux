package com.ijse.gdse.cw.hasitha.userService.configuration.security;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.server.authentication.ServerAuthenticationConverter;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Component
public class AuthConverter implements ServerAuthenticationConverter {


    @Override
    public Mono<Authentication> convert(ServerWebExchange exchange) {
        return Mono.justOrEmpty(
                exchange.getRequest()
                        .getHeaders()
                        .getFirst("Authorization")
        ).filter(authHeader -> authHeader.startsWith("Bearer "))
                .map(authHeader -> authHeader.substring(7))
                .map(BearerToken::new);
    }
}

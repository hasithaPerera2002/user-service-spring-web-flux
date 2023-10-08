package com.ijse.gdse.cw.hasitha.userService.service;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import lombok.Setter;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.Objects;

@Service
public class JWTServices {
    private final SecretKey secretKey;

    private final JwtParser jwtParser;

    public JWTServices() {
        this.secretKey = Keys.secretKeyFor(SignatureAlgorithm.HS256);
        this.jwtParser = Jwts.parserBuilder().setSigningKey(this.secretKey).build();
    }

    public String generate(String email){

        JwtBuilder jwtBuilder = Jwts.builder()
                .setSubject(email)
                .setIssuedAt(Date.from(Instant.now()))
                .setExpiration(Date.from(Instant.now().plus(15, ChronoUnit.MINUTES)))
                .signWith(this.secretKey);
        return jwtBuilder.compact();
    }
    public String getEmail(String token){
        Claims claims = this.jwtParser.parseClaimsJws(token).getBody();
       return claims.getSubject();
    }

    public boolean validate(String token,String email){
    Claims claims = this.jwtParser.parseClaimsJws(token).getBody();
        boolean unexpired = claims.getExpiration().after(Date.from(Instant.now()));
        return unexpired && Objects.equals(email, claims.getSubject());
    }
}

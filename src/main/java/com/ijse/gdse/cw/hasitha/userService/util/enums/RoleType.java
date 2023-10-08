package com.ijse.gdse.cw.hasitha.userService.util.enums;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@RequiredArgsConstructor
public enum RoleType {
    USER,
    ADMIN_USER_SERVICE,
    ADMIN_TRAVEL_SERVICE,
    ADMIN_HOTEL_SERVICE,
    ADMIN_VEHICLE_SERVICE,
    ADMIN_GUIDE_SERVICE;

    public List<SimpleGrantedAuthority> getAuthorities() {
        return Stream.concat(
                Stream.of(new SimpleGrantedAuthority("ROLE_" + this.name())),
                Stream.of(this.name()).map(SimpleGrantedAuthority::new)
        ).collect(Collectors.toList());
    }
}

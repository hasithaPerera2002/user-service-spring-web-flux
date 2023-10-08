package com.ijse.gdse.cw.hasitha.userService.entity;
import com.ijse.gdse.cw.hasitha.userService.util.enums.Gender;
import com.ijse.gdse.cw.hasitha.userService.util.enums.RoleType;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.bson.types.Binary;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.validation.constraints.*;
import java.util.Collection;
import java.util.List;


@Document(collection = "user")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class User implements UserDetails {
    @Id
    @NotNull
    @NotBlank(message = "User ID cannot be blank")
    private String userID;

    @NotBlank(message = "Username cannot be blank")
    @NotNull(message = "Username cannot be null")
    private String username;

    @NotBlank(message = "Password cannot be blank")
    @NotNull(message = "Password cannot be null")
    private String password;

    @Email(message = "Email should be a valid email address")
    @NotNull(message = "Email cannot be null")
    private String email;

    @Positive(message = "Age should be a positive integer")
    @NotNull(message = "Age cannot be null")
    @   Min(value = 18, message = "Age should be greater than or equal to 18")
    @Max(value = 80, message = "Age should be less than or equal to 80")
    private Integer age;

    @Size(max = 20, message = "Contact number should not exceed 20 characters")
    @NotNull(message = "Contact number cannot be null")
    private String contactNumber;

    @Size(max = 100, message = "Address should not exceed 100 characters")
    @NotNull(message = "Address cannot be null")
    private String address;

    @NotNull(message = "Role cannot be null")
    private RoleType role;


    private Binary profilePicture;

    @NotNull
    private Gender gender;

    @Size(max = 255, message = "Remark should not exceed 255 characters")
    private String remark;

    private Binary frontImageOfNIC;

    private Binary backImageOfNIC;

    @Size(max = 10, message = "NIC number should not exceed 10 characters")
    private String nicNumber;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return role.getAuthorities();
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
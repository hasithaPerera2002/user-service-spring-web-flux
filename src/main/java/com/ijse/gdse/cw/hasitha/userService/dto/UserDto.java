package com.ijse.gdse.cw.hasitha.userService.dto;

import com.ijse.gdse.cw.hasitha.userService.util.enums.Gender;
import com.ijse.gdse.cw.hasitha.userService.util.enums.RoleType;
import javax.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.Binary;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class UserDto {
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
    @Min(value = 18, message = "Age should be greater than or equal to 18")
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

    @NotNull(message = "Gender cannot be null")
    private Gender gender;

    @Size(max = 255, message = "Remark should not exceed 255 characters")
    private String remark;

    @NotNull(message = "Front image of NIC cannot be null")
    private Binary frontImageOfNIC;

    @NotNull(message = "Back image of NIC cannot be null")
    private Binary backImageOfNIC;

    @Size(max = 10, message = "NIC number should not exceed 10 characters")
    private String nicNumber;
}
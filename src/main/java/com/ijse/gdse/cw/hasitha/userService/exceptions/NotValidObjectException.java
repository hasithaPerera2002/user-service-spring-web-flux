package com.ijse.gdse.cw.hasitha.userService.exceptions;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NotValidObjectException extends RuntimeException {
    private Set<String> violations;
}

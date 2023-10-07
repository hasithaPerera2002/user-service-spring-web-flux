package com.ijse.gdse.cw.hasitha.userService.util;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ResponseUtil {
    int code;
    String message;
    Object data;
}

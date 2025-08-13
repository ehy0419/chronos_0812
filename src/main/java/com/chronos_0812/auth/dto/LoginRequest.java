package com.chronos_0812.auth.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
public class LoginRequest {

    private String email;
    private String password;
//    private String username;
}

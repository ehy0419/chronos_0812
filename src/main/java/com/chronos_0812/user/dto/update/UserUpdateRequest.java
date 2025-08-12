package com.chronos_0812.user.dto.update;

import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 유저 수정 요청 DTO
 */
@Getter
@NoArgsConstructor

public class UserUpdateRequest {

    private String username;
    private String email;

}

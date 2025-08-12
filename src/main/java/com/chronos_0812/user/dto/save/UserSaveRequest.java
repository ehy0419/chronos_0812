package com.chronos_0812.user.dto.save;

import lombok.Getter;
import lombok.NoArgsConstructor;

/** 유저 생성 요청 DTO
 * - Lv5에서 @NotBlank, @Email, @Size 등의 Bean Validation 추가 예정
 *
 * <주의 또는 제안?
 * 수정 요청 DTO(UserUpdateRequest)는 비밀번호 제외(일반 정보 변경만)
 * 다음에 별도 ChangePasswordRequest로 분리 도전해보기.
 * </주의>
 */

@Getter
@NoArgsConstructor
public class UserSaveRequest {

    private String username;
    private String email;
    private String password;

}

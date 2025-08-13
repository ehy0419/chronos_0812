package com.chronos_0812.user.dto.save;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

/** 유저 생성 요청 DTO
 * - Lv5에서 @NotBlank, @Email, @Size 등의 Bean Validation 추가 예정
 *
 * <주의 또는 제안?
 * 수정 요청 DTO(UserUpdateRequest)는 비밀번호 제외(일반 정보 변경만)
 * 다음에 별도 ChangePasswordRequest로 분리 도전해보기.
 * </주의>
 *
 *          /// 비밀번호는 요청 '받기'만 하자 ///
 */

@Getter
@NoArgsConstructor
@ToString
public class UserSaveRequest {

    private String username;
    private String email;

    // 수정 전: 평범한 필드 (String password) → 응답/로그에 노출 가능성 높음
    // 수정 후: WRITE_ONLY + ToString.Exclude 로 응답/로그 비노출
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)      // 응답 JSON에 안 나감
    @ToString.Exclude                                           // `서버 로그(toString)`에도 마스킹
    private String password;
}

/**
 * [비밀번호 번호 보안 대책]
 - WRITE_ONLY: 요청으로만 받음, 응답(JSON)에는 절대 표시되지 않음
 -  ToString.Exclude: `서버 로그(toString)`에도 마스킹
 */

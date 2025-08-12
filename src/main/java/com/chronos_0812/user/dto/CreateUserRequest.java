package com.chronos_0812.user.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CreateUserRequest(
        @NotBlank
        @Size(max = 30)
        String username,

        @NotBlank
        @Email
        @Size(max = 100)
        String email,

        @NotBlank
        @Size(max = 100)
        String password
) { }

/**
 * 레코드 클래스를 사용하는 이유 - DTO의 불변성과 간결함
 * 모든 필드가 final이며, 생성 시 값이 고정되어 변경 불가능
 * 생성자, getter, equals/haschcode, toString 을 자동생성해 보일러 플레이트 코드 제거
 * 데이터 전달을 목적으로 하는 DTO에 최적화
 */

/**
 * @NotBlack @Size 어노테이션 사용을 위해서 의존성 추가
 * dependencies {
 *     implementation 'org.springframework.boot:spring-boot-starter-validation'
 * }
 */


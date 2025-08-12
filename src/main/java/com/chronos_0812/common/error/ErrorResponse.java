package com.chronos_0812.common.error;

import lombok.Getter;

import java.time.LocalDateTime;

/**
 *      API 예외 응답을 담는 DTO
 *
 *      'message'                   클라이언트에게 전달할 오류 메시지
 *      추후 확장 시 상태 코드, 요청 경로, 타임스탬프 등을 추가할 수 있음
 */

@Getter
public class ErrorResponse {
    private final String message;               // 오류 메시지
    private final LocalDateTime timestamp;

    // 생성자
    public ErrorResponse(String message) {
        this.message = message;
        this.timestamp = LocalDateTime.now();
    }
}

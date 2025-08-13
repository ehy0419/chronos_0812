package com.chronos_0812.common.error;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 *              #### IllegalStateException 메서드 ####
 * 1) 언제 개입하는가???
 * "요청의 현재 상태가 처리 불가" 일 때 던지는 예외르 전담 처리합니다.
 * ": 비밀번호 불일치, 이미 처리된 리소스에 대한 중복 요청 등
 *
 * 2) 파라미터
 * (IllegalStateException e) : 스프링이 발생한 예외 객체를 주입합니다.
 *
 * 3) 반환
 * ResponseEntity<ErrorResponse> : HTTp 상태코드와 에러 본문을 함께 담아 반환
 *
 * 4) 주의
 * e.getMessage() : 클라이언트에게 그대로 노출되므로, 내부 정보 와 민감한 정보는 포함되지 않도록 예외 메시지를 설계하세요
 * 로그에는 상세, 응답에는 요약 메시지를 권장합니다
 * 상태 코드는 상황에 따라 조정 가능합니다
 * 예 : 권한 문제는 403, 인증 실패는 401 등
 */

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(IllegalArgumentException.class)      // 이 타입(및 하위 타입)의 예외를 이 메서드가 처리

    /**
     * HTTP 400 (Bad Request)로 응답 : 클라이언트의 요청 상태가 비즈니스 규칙상 유효하지 않다
     */
    public ResponseEntity<ErrorResponse> handleIllegalArgument(IllegalArgumentException e) {
        // 수정 전
        //public ResponseEntity<ErrorResponse> handleIllegalStateException(IllegalStateException e) {
        // 수정 후
        //public ResponseEntity<ErrorResponse> handleIllegalArgument(IllegalArgumentException e) {

        ErrorResponse errorResponse = new ErrorResponse(e.getMessage());
        // 1) 예외 메시지를 응답DTO로 감싼다.
        //          ErrorResponse는 API 응답 표준화의 핵심 - 필요한 필드를 추가해 일관된 사용을 권장합니다.

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(errorResponse);
        // 2) HTTP 400 (Bad Request)로 응답한다.
        //          "클라이언트의 요청 상태가 비즈니스 규칙상 유효하지 않다"는 의미에 적합
        //          상황에 따라 409 (conflict), 402 (unprocessable_entitiy) 등으로 매핑 가능
    }

    /**
     * HTTP 409 (conflict) 응답 - 이메일 중복 검증 - IllegalStateException 에서 사용
     */
    // version 1
//    @ExceptionHandler(IllegalStateException.class)
//    public ResponseEntity<ErrorResponse> handleIllegalState(IllegalStateException e) {
//        return ResponseEntity
//                .status(HttpStatus.CONFLICT)
//                .body(new ErrorResponse(e.getMessage()));
//    }
    // version 2
    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<ErrorResponse> handleIllegalState(IllegalStateException e) {
        ErrorResponse errorResponse = new ErrorResponse(e.getMessage());
        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(errorResponse);
    }

}

// IllegalStateException -> HTTP 400 (Bad Request) 코드
// unprocessable_entitiy -> 402 코드 - 오타 검증?
// conflict              -> 409 코드 - 상태 충돌  -> 이메일 중복??
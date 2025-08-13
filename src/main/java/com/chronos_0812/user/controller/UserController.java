package com.chronos_0812.user.controller;

import com.chronos_0812.user.dto.get.UserGetAllResponse;
import com.chronos_0812.user.dto.get.UserGetOneResponse;
import com.chronos_0812.user.dto.save.UserSaveRequest;
import com.chronos_0812.user.dto.save.UserSaveResponse;
import com.chronos_0812.user.dto.update.UserUpdateRequest;
import com.chronos_0812.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 유저 CRUD API
 * - POST   /users
 * - GET    /users
 * - GET    /users/{userId}
 * - PUT    /users/{userId}
 * - DELETE /users/{userId}
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    @PostMapping
    // 수정 전
//    public ResponseEntity<Long> save(
//            @RequestBody UserSaveRequest userSaveRequestreq
//    ) {
//        return ResponseEntity.ok(userService.save(userSaveRequestreq));
//    }

    // 수정 후
    public ResponseEntity<UserSaveResponse> save(
            @RequestBody UserSaveRequest userSaveRequest
    ) {
        return ResponseEntity.ok(userService.save(userSaveRequest));
    }


    @GetMapping
    public ResponseEntity<List<UserGetAllResponse>> findAll() {
        return ResponseEntity.ok(userService.findAll());
    }

    @GetMapping("/{userId}")
    public ResponseEntity<UserGetOneResponse> findOne(
            // 수정 전 : @PathVariable Long id
            // 수정 후 : @PathVariable("userId") Long userId
            // 수정 이유 : /// PathVariable 이름 불일치: 경로는 {userId}인데 파라미터는 id → @PathVariable("userId")로 명시 매핑
            @PathVariable("userId") Long userId
    ) {
        return ResponseEntity.ok(userService.findOne(userId));
    }

    @PutMapping("/{userId}")
    public ResponseEntity<Void> update(
            @PathVariable("userId") Long userId,
            @RequestBody UserUpdateRequest userUpdateRequest
    ) {
        userService.update(userId, userUpdateRequest);
        return ResponseEntity.noContent().build();
    }

    // 수정 전
//    @DeleteMapping("/{userId}")
//    public ResponseEntity<Void> delete(
//            @PathVariable("userId"
//    ) {
//        userService.delete(id);
//        return ResponseEntity.noContent().build();
//    }

    // 수정 후
    @DeleteMapping("/{userId}")
    public ResponseEntity<Void> delete(
            @PathVariable("userId") Long userId
    ) {       // 수정 후: 이름 매핑
        userService.delete(userId);
        return ResponseEntity.noContent().build();
    }

/* ---------------------- .build() 메서드 설명(주석) ----------------------

[무엇을 하는 메서드인가?]
- DELETE /users/{userId} 요청을 처리합니다.
- @PathVariable("userId") : URL 템플릿 변수 이름을 파라미터에 "명시적으로" 매핑합니다.
  (메서드 파라미터명이 다르거나 리팩터링해도 안전)

[사용하는 이유는 무엇인가?]
- 생성자 방식에는 순서가 중요한데, 헷갈리기 쉽습니다.
   // new UserGetOneResponse(1L, "alice", "a@a.com", createdAt, modifiedAt);

[.build()가 사용되는 방식]
   // UserGetOneResponse resp = UserGetOneResponse.builder()
   //        .id(1L)
   //        .username("alice")
   //        .email("a@a.com")
   //        .createdAt(createdAt)
   //        .modifiedAt(modifiedAt)
   //        .build(); // ← 여기서 진짜 객체 생성!
- builder() : 빈 빌더를 하나 만들고
- id(...), username(...) 같은 메서드로 필드값을 담아두고
- build() : 담아둔 값으로 완성품 객체를 반환합니다.

[.build()를 이유하기 쉬울 비유]
- 피자 주문
- builder() = 빈 주문서 받기
- id(...), username(...) = 토핑 고르기
- build() = “굽기” 눌러서 피자 완성 → 결과물(객체) 받기

[예시로 유저 응답 DTO를 가지고 .build()의 사용례를 보면]
   // public class UserGetOneResponse {
   //    // 필드들...
   //    // 생성자/게터...
   //
   //    public static class UserGetOneResponseBuilder {
   //        private Long id;
   //        private String username;
   //        private String email;
   //        private LocalDateTime createdAt;
   //        private LocalDateTime modifiedAt;
   //
   //        public UserGetOneResponseBuilder id(Long id) { this.id = id; return this; }
   //        public UserGetOneResponseBuilder username(String v) { this.username = v; return this; }
   //        // ... 다른 필드들도 같은 패턴
   //
   //        public UserGetOneResponse build() {
   //            // 여기서 진짜 객체를 만들어서 돌려줌
   //            return new UserGetOneResponse(id, username, email, createdAt, modifiedAt);
   //        }
   //    }
   //
   //    public static UserGetOneResponseBuilder builder() {
   //        return new UserGetOneResponseBuilder();
   //    }
   //}
- 즉, .build()가 new UserGetOneResponse(...)를 대신 호출한다.

[ResponseEntity.noContent().build() 의미]
- 204 No Content 응답을 생성합니다. (본문 없이 “성공적으로 처리됨”을 의미)
- noContent() 가 ResponseEntity.BodyBuilder 를 반환하고,
  마지막에 build() 가 “최종 ResponseEntity” 객체를 만들어 돌려줍니다.

[여기서의 .build()는 Lombok 빌더가 아님!]
- 이 build() 는 Spring 의 ResponseEntity.BodyBuilder 의 build() 입니다.
- 개념은 비슷: “모아둔 설정(상태코드/헤더)으로 최종 객체 완성”.
- Lombok @Builder 의 build() 와 대상만 다르고, “완성 버튼”이라는 점은 동일해요.

[.build() 없이 작성하는 대안들]
1) (생성자 직접 사용)
   // return new ResponseEntity<>(HttpStatus.NO_CONTENT);

2) (정적 팩토리 + body(null) 사용)
   // return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);

3) (헤더가 필요할 때 — 빌더 체인 유지)
   // return ResponseEntity.noContent()
   //         .header("X-Deleted-Id", String.valueOf(userId))
   //         .build();


[REST 관례 팁]
- 생성(POST): 201 Created + Location 헤더
- 삭제(DELETE): 204 No Content 권장
- “이미 없는 리소스 삭제” 시 204 또는 404 중 하나로 팀 규칙 통일 권장
  (현재 서비스는 예외를 던지므로 @ControllerAdvice 로 404/409 등 상태코드 매핑 권장)

------------------------------------------------------------------- */
}
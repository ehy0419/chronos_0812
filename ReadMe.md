
참고
띄어쓰기
&nbsp;
&ensp;
&emsp;

# 프로젝트 패키지 구조
## Lv1. 일정 CRUD <br>

```
com.example.schedule
 ├── controller       // 요청/응답 처리
 │     └── ScheduleController.java
 ├── dto              // 요청·응답 DTO
 │     ├── save
 │     │     └── ScheduleSaveRequest.java
 │     ├── get
 │     │     ├── ScheduleGetAllResponse.java
 │     │     └── ScheduleGetOneResponse.java
 │     └── update
 │           └── ScheduleUpdateRequest.java
 ├── entity           // JPA 엔티티
 │     └── Schedule.java
 ├── repository       // DB 접근
 │     └── ScheduleRepository.java
 ├── service          // 비즈니스 로직
 │     └── ScheduleService.java
 └── common           // 공통 기능 (Auditing BaseEntity 등)
       └── BaseEntity.java

```

Lv1. 점검할 것. <br>
* 필수 패키지: controller, dto, entity, repository, service, common<br>
* 필수 클래스: <br>
  * Entity: Schedule
  * DTO: 저장/수정/조회 DTO
  * Repository: ScheduleRepository
  * Service: ScheduleService
  * Controller: ScheduleController
  * BaseEntity: JPA Auditing용

## Lv2. 유저 CRUD <br>
```
src/main/java/com/chronos_0812
 ├─ config
 │   └─ JpaAuditingConfig.java              // (Lv1 재사용)
 ├─ common
 │   └─ BaseEntity.java                     // (Lv1 재사용)
 ├─ user
 │   ├─ controller
 │   │   └─ UserController.java
 │   ├─ dto
 │   │   ├─ get
 │   │   │   ├─ UserGetAllResponse.java
 │   │   │   └─ UserGetOneResponse.java
 │   │   ├─ save
 │   │   │   └─ UserSaveRequest.java
 │   │   └─ update
 │   │       └─ UserUpdateRequest.java
 │   ├─ entity
 │   │   └─ User.java
 │   ├─ repository
 │   │   └─ UserRepository.java
 │   └─ service
 │       └─ UserService.java
 └─ schedule
     ├─ controller
     │   └─ ScheduleController.java         // (수정) 저장 DTO 변경
     ├─ dto
     │   ├─ get
     │   │   ├─ ScheduleGetAllResponse.java // (수정) username, userId 포함
     │   │   └─ ScheduleGetOneResponse.java // (수정) username, userId 포함
     │   ├─ save
     │   │   └─ ScheduleSaveRequest.java    // (변경) author → userId
     │   └─ update
     │       └─ ScheduleUpdateRequest.java
     ├─ entity
     │   └─ Schedule.java                   // (수정) author → user 연관관계
     ├─ repository
     │   └─ ScheduleRepository.java
     └─ service
         └─ ScheduleService.java            // (수정) 저장 시 userId 로드

```
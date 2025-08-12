
참고
띄어쓰기
&nbsp;
&ensp;
&emsp;

# 프로젝트 패키지 구조
## Lv1. 일정 CRUD <br>

```json
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
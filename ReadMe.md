
# 일정관리 Chronos_0812
시간의 신 크로노스 이름을 따서 이름을 지었습니다.
일정(Schedule)과 유저(User) 관리 API 입니다.

'0812' 날짜는 과제에 필요하다고 생각되는 코드 소스들을 합쳐서 과제를 만들기 시작한 날입니다.

##  다음을 포함합니다
Spring Boot 3.5.4.<br>
JPA<br>
Validation<br>
세션 로그인 (구동 미확인)<br>
비밀번호 해시화 (도전)<br>

## 의존성 추가 (buil.gradle)
```java
dependencies {
    implementation 'org.springframework.boot:spring-boot-starter-data-jpa'
    implementation 'org.springframework.boot:spring-boot-starter-web'
    implementation 'org.springframework.boot:spring-boot-starter-validation'    // Bean Validation을 위한 의존성 추가
    implementation 'at.favre.lib:bcrypt:0.10.2'                                 // Bcrypt 라이브러리
    compileOnly 'org.projectlombok:lombok'
    runtimeOnly 'com.mysql:mysql-connector-j'
    annotationProcessor 'org.projectlombok:lombok'
    testImplementation 'org.springframework.boot:spring-boot-starter-test'
    testRuntimeOnly 'org.junit.platform:junit-platform-launcher'
}
```

## application.yml 설정
```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/{데이터이름}
    username: 유저이름
    password: 비밀번호
    driver-class-name: com.mysql.cj.jdbc.Driver

  jpa:
    hibernate:
      ddl-auto: update
    properties:
      hibernate:
        format_sql: true
        show_sql: true
    open-in-view: false

server:
  port: 8080

```


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

**Lv1.점검 체크리스트**
- 필수 패키지: `controller`, `dto`, `entity`, `repository`, `service`, `common`
- 필수 클래스
  - Entity: `Schedule`
  - DTO: 저장/수정/조회(전체 및 단건) DTO
  - Repository: `ScheduleRepository`
  - Service: `ScheduleService`
  - Controller: `ScheduleController`
  - BaseEntity: JPA Auditing용

## Lv2. 유저 CRUD + 일정과 연관관계 (일정 작성자 author → user)
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

## ERD (Entity Relationship Diagram)

```mermaid
ERDiagram
    USER ||--o{ SCHEDULE : writes

    USER {
        BIGINT id PK
        VARCHAR(30) username
        VARCHAR(100) email UNIQUE
        VARCHAR(100) password   // BCrypt hash, 응답/로그 미노출
        DATETIME createdAt      // DATEBY 는 작성한 사람
        DATETIME modifiedAt     // DATETIME 은 작성한 시각
    }

    SCHEDULE {
        BIGINT id PK
        VARCHAR(30) title
        VARCHAR(200) content
        BIGINT user_id FK       // -> USER.id
        DATETIME createdAt
        DATETIME modifiedAt
    }
```
---

## 수정 및 추가 도전과제
* 포스트맨과 필드 입력에 대한 미숙지로 일정CRUD 부터 확인
* `COMMENT` 엔티티를 추가하여 `SCHEDULE 1:N COMMENT` 구조로 확장 예정 (요구사항에 맞춰 최대 10개 제한 등).
* 카멜케이스 또는 낙타케이스 필드 변수의 일원화
* 회원가입, 로그인, 로그아웃 기능 구현의 미확인
* 인증 인가 필터에서 화이트리스트 구성 경로 수정 확인
* 예외코드 확인하기
* Validation 추가해보기

---

## 공통 응답 형식 & 예외 처리

### 성공/실패 응답 공통 포맷
- 성공: API Response DTO 에서 참조해주세요
- 실패: `ErrorResponse`

```json
{
  "message": "오류 설명",
  "timestamp": "2025-08-14T10:00:00"
}
```
// 기대했던 응답이지만 포스트맨 이용과 URL 숙지를 등한시해서 

### 예외 처리 확인
- `IllegalArgumentException` → **400 Bad Request**
- 이메일 중복/상태 충돌 등 `IllegalStateException` → **409 Conflict**
- 인증 실패 → **401 Unauthorized**

> 컨트롤러 단에서 던진 예외는 `@RestControllerAdvice`의 `GlobalExceptionHandler`에서 위 규칙으로 일관되게 변환합니다.

---

## API 명세

### 인증/세션
- 회원가입: `POST /auth/signup`
- 로그인: `POST /auth/login` → 성공 시 `JSESSIONID` 세션 쿠키 발급 및 서버 메모리에 세션 저장
- 로그아웃: `POST /auth/logout` → 세션 무효화

> 보호가 필요한 API는 세션이 존재해야 접근 가능하도록 **서블릿 필터**로 보호합니다. 
> 필터 화이트리스트에 `/signup`, `/login` 포함 필요.

---

### 1) 일정(Schedule) API

#### 1.1 일정 생성
| 항목 | 내용                                                                        |
|---|---------------------------------------------------------------------------|
| **Method** | `POST`                                                                    |
| **URL** | `/schedules`                                                              |
| **Request Body** | `title` 일정 제목 : String, 필수, 30자 이상)                                       
|| `content` 일정 내용 : String, 필수, 200자 이상)                                    
|| `userId` 유저 Id : Long, 필수                                                 |
| **Response** | `201 Created`                  |
| **Response Body** | `id`, `title`, `content`, `userId`, `username`, `createdAt`, `modifiedAt` |
| **Error** | `400 Bad Request` - 필수값 누락/길이 초과/존재하지 않는 유저                               |

---

#### 1.2 일정 목록 조회
| 항목 | 내용                                                                               |
|---|----------------------------------------------------------------------------------|
| **Method** | `GET`                                                                            |
| **URL** | `/schedules`                                                                     |
| **Query** | `userId` 유저 Id : Long, 선택 - 해당 유저의 일정만 필터                                        |
| **Response** | `200 OK`                                                                         |
| **Response Body** | 일정 배열: `id`, `userId`, `username`, `title`, `content`, `createdAt`, `modifiedAt` |


---

#### 1.3 일정 단건 조회
| 항목 | 내용                                                                        |
|---|---------------------------------------------------------------------------|
| **Method** | `GET`                                                                     |
| **URL** | `/schedules/{scheduleId}`                                                 |
| **Path** | `scheduleId` 일정Id : Long, 필수                                              |
| **Response** | `200 OK`                                                                  |
| **Response Body** | `id`, `userId`, `username`, `title`, `content`, `createdAt`, `modifiedAt` |
| **Error** | `400 Bad Request` - 존재하지 않는 일정                                            |

---

#### 1.4 일정 수정 (부분 수정)
| 항목 | 내용                                              |
|---|-------------------------------------------------|
| **Method** | `PATCH`                                         |
| **URL** | `/schedules/{scheduleId}`                       |
| **Path** | `scheduleId` 일정Id : Long, 필수                    |
| **Request Body** | `title` 일정 제목 : String, 선택, 30자 이상, 빈 문자열 불가    
|| `content` 일정 내용 : String, 선택, 200자 이상, 빈문자열 불가 |
| **Response** | `200 OK` (수정된 리소스)                              |
| **Error** | `400 Bad Request` - 길이 초과/빈문자열/존재하지 않는 일정       |

---

#### 1.5 일정 삭제
| 항목 | 내용                             |
|---|--------------------------------|
| **Method** | `DELETE`                       |
| **URL** | `/schedules/{scheduleId}`      |
| **Path** | `scheduleId` 일정 Id : Long, 필수  |
| **Response** | `204 No Content`               |
| **Error** | `400 Bad Request` - 존재하지 않는 일정 |

---

### 2) 유저(User) API

#### 2.1 회원가입(유저 생성)
| 항목 | 내용                                                    |
|---|-------------------------------------------------------|
| **Method** | `POST`                                                |
| **URL** | `/users` *(또는)* `/auth/signup`                        |
| **Request Body** | `username` 유저 명 : String, 필수, 30자 이상                  
|| `email` 유저 이메일 : String, 필수, 100자 이상, UNIQUE          
|| `password` 유저 비밀번호 : String, 필수, 100자 이상 — 응답/로그 미노출) |
| **Response** | `201 Created` *(현재 컨트롤러는 200 OK 응답)*                  |
| **Response Body** | `id`, `username`, `email`, `createdAt`, `modifiedAt`  |
| **Error** | `400 Bad Request` - 필수값 누락/이메일 형식 오류                  
||`409 Conflict` - 이메일 중복(정책에 따라 400 처리 가능) |

> 비밀번호는 **BCrypt 해시로 저장**하고, 응답과 로그에서 **절대 노출하지 않습니다**.

---

#### 2.2 유저 전체 조회
| 항목 | 내용 |
|---|---|
| **Method** | `GET` |
| **URL** | `/users` |
| **Response** | `200 OK` |
| **Response Body** | 유저 배열: `id`, `username`, `email`, `createdAt`, `modifiedAt` |

---

#### 2.3 유저 단건 조회
| 항목 | 내용 |
|---|---|
| **Method** | `GET` |
| **URL** | `/users/{userId}` |
| **Response** | `200 OK` |
| **Error** | `400 Bad Request` - 존재하지 않는 유저 |

---

#### 2.4 유저 수정 (비밀번호 제외)
| 항목 | 내용 |
|---|---|
| **Method** | `PUT` |
| **URL** | `/users/{userId}` |
| **Request Body** | `username` 필수
||`email` 필수, UNIQUE |
| **Response** | `204 No Content` |
| **Error** | `400 Bad Request` - 필수값 누락/형식 오류  \
||`409 Conflict` - 이메일 중복 |

---

#### 2.5 유저 삭제
| 항목 | 내용 |
|---|---|
| **Method** | `DELETE` |
| **URL** | `/users/{userId}` |
| **Response** | `204 No Content` |
| **Error** | `400 Bad Request` - 존재하지 않는 유저 |

---

### 3) 인증(Auth) API

#### 3.1 회원가입
| 항목 | 내용 |
|---|---|
| **Method** | `POST` |
| **URL** | `/auth/signup` |
| **Body** | `username`, `email`, `password` |
| **Response** | `200 OK` *(또는 201 Created 권장)* |

#### 3.2 로그인
| 항목 | 내용 |
|---|---|
| **Method** | `POST` |
| **URL** | `/auth/login` |
| **Body** | `email`, `password` |
| **Response** | `200 OK` + `Set-Cookie: JSESSIONID=...` |
| **Response Body** | `userId`, `username` |
| **Error** | `401 Unauthorized` - 이메일/비밀번호 불일치 |

#### 3.3 로그아웃
| 항목 | 내용 |
|---|---|
| **Method** | `POST` |
| **URL** | `/auth/logout` |
| **Response** | `204 No Content` |

---

## 유효성(Validation) & 제약
- `Schedule.title` ≤ 30, `content` ≤ 200 — 공백 불가
- `User.username` ≤ 30, `email` ≤ 100 (UNIQUE, 중복 불가), `password` ≤ 100 — 공백 불가
- `@Valid` / `@Size` / `@NotBlank` / `@NotNull` 적절히 사용
- 업데이트(PATCH/PUT) 시 빈 문자열 금지, 길이 초과 금지

---

## 보안/로그 정책
- 비밀번호: BCrypt 해시로 저장 (`PasswordEncoder.encode`), 평문 보관 금지
- 비교: `PasswordEncoder.matches(raw, hash)` 사용(재해시 금지)
- DTO/로그: 비밀번호 **WRITE_ONLY** + `@ToString.Exclude`로 응답/로그 노출 금지
- 세션: 로그인 성공 시 `SessionContainer.LOGIN_USER` 키로 최소 정보(`id`, `username`, `email`) 저장, 타임아웃 30분 예시
- 필터: 화이트리스트(`/auth/signup`, `/auth/login`, `/`) 외 요청은 로그인 세션 필수

---

## 변경 이력 (요약)
- **Lv2**: `Schedule.author(String)` → `Schedule.user(User)` 연관관계로 변경  \
  저장 요청 DTO `author` → `userId`, 조회 응답에 `username`, `userId` 포함
- 비밀번호 저장 로직 추가: BCrypt 해시로 저장, 응답/로그 노출 금지
- 전역 예외 처리기 추가: `IllegalArgumentException`→400

---
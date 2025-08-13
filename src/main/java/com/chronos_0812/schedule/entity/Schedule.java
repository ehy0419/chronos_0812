package com.chronos_0812.schedule.entity;

import com.chronos_0812.common.config.BaseEntity;
import com.chronos_0812.user.entity.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * Lv1 요구사항:
 * - 작성 유저명(author), 할일 제목(title), 할일 내용(content), 작성일(createdAt), 수정일(modifiedAt)
 * - 작성일 (createdAt)과 수정일 (modifiedAt)은 BaseEntity(JPA Auditing)로 자동 관리
 *
 * Lv2 변경점:
 * - (이전) author:String  → (현재) user:User (ManyToOne)
 */

@Entity
@Table(name = "schedules")                              // 일정 명찰 설정
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)      // JPA 규약상 기본 생성자(무인자)가 반드시 필요
public class Schedule extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** 할일 제목 (필수, 최대 30자) */
    @Column(nullable = false, length = 30)
    private String title;

    /** 할일 내용 (필수, 최대 200자) */
    @Column(nullable = false, length = 200)
    private String content;

    /** 작성자 - LV1. String → LV2. User 연관관계(ManyToOne) */
    // 수정 전 : private String author;
    // 수정 후 : private User user;  (DB에는 user_id(FK)로 저장됨)
    // 결론 : 일정은 User 엔티티 고유식별자(id)를 FK로 참조하는 구조로 변경한다.
    /** 작성 유저명 (필수, 최대 30자) */
    @ManyToOne(fetch = FetchType.LAZY, optional = false)        // 수가 많을 곳에 작성!
    @JoinColumn(name = "user_id", nullable = false)             // @Column이 아니라 @JoinColumn 사용!
    private User user;

    // 생성자 - 필수 필드 세팅
    public Schedule(String title, String content, User user) {
        this.title = title;
        this.content = content;
        this.user = user;
    }

    // 필요한 부분만 메서드 제공 - 일정 작성자는 변경 X
    // 1st.
//    수정 메서드
//    public void update(String title, String content) {
//        this.title = title;
//        this.content = content;
//    }

    /** 엔티티 내부의 변경점은 아래와 같이 메서드로 나눠두면 추적이 쉽다. */
    // 2nd. 나눠서
    // 부분 수정 메서드 1 - 일정 제목 변경
    public void updateTitle(String title) {
        this.title = title;
    }

    // 부분 수정 메서드 2 - 일정 내용 변경
    public void updateContent(String content) {
        this.content = content;
    }
}

// 오류문 : Caused by: org.hibernate.AnnotationException: Property 'com.chronos_0812.schedule.entity.Schedule.author' is a '@ManyToOne' association and may not use '@Column' to specify column mappings (use '@JoinColumn' instead)
/**
 * 오류원인 : Hibernate가 Schedule 엔티티의 author 필드가 @ManyToOne 관계인데,
 * 거기에 @Column을 붙였기 때문에 발생하는 문제입니다.
 * @Column은 단순 값 타입(String, int, LocalDateTime 등)에만 사용합니다.
 * 반면, @ManyToOne은 다른 엔티티와의 관계를 나타내기 때문에, 컬럼 매핑은 @JoinColumn을 사용해야 합니다.
 * 즉, @Column은 VARCHAR, INT 등 단일 필드 매핑에 쓰이고,
 * @JoinColumn은 외래키(FK) 관계에서 매핑을 담당합니다.
 */


package com.chronos_0812.schedule.entity;

import com.chronos_0812.common.base.BaseEntity;
import com.chronos_0812.user.entity.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * Lv1 요구사항:
 * - 작성 유저명(author), 할일 제목(title), 할일 내용(content), 작성일(createdAt), 수정일(updatedAt)
 * - 작성일/수정일은 BaseEntity(JPA Auditing)로 자동 관리
 */

@Entity
@Table(name = "schedules")                              // 일정 명찰 설정
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)      // JPA 규약상 기본 생성자(무인자)가 반드시 필요
public class Schedule extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**할일 제목 (필수, 최대 30자) */
    @Column(nullable = false, length = 30)
    private String title;

    /** 할일 내용 (필수, 최대 30자) */
    @Column(nullable = false, length = 200)
    private String content;

    /** 작성 유저명 (필수, 최대 30자) */
    ///  LV1. 작성자명 - LV2. 사용자 연관관계(Many To one)
    @ManyToOne(fetch = FetchType.LAZY)                      // 수가 많을 곳에 작성!
    @JoinColumn(name = "user_id", nullable = false)  //FK       // nullable = false 이기때문에 getAuthor().getUsername() NPE 위험 낮음.
    private User author;
    // 수정 전 : private String author;
    // 수정 후 : private User author;
    // 작성 유저명 - 객체(id)로 참조
    // db에 외래키(user_id)로 생성됨
    // 결론 : 일정은 User 엔티티 고유식별자(id)를 FK로 참조하는 구조로 변경한다.

    // 생성자 - 필수 필드 세팅
    public Schedule(String title, String content, User author) {
        this.title = title;
        this.content = content;
        this.author = author;
    }

    // 필요한 부분만 메서드 제공 - 일정 작성자는 변경 X
    // 1st.
//    수정 메서드
//    public void update(String title, String content) {
//        this.title = title;
//        this.content = content;
//    }

    /** 엔티티 내부의 변경점은 아래 같이 메서드로 감싸두면 추적이 쉽다. */
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


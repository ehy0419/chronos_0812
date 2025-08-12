package com.chronos_0812.schedule.entity;

import com.chronos_0812.common.BaseEntity;
import com.chronos_0812.user.entity.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)      // JPA 규약상 기본 생성자(무인자)가 반드시 필요
public class Schedule extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 30)      // 제목은 필수, 최대 30자
    private String title;                       // 할 일 제목
    @Column(nullable = false, length = 200)     // 내용은 필수, 최대 200자
    private String content;                     // 할 일 내용

    ///  LV1. 작성자명 - LV2. 사용자 연관관계(Many To one)
    @ManyToOne(fetch = FetchType.LAZY)                      // 수가 많을 곳에 작성!
    @JoinColumn(name = "user_id", nullable = false)         // nullable = false 이기때문에 getAuthor().getUsername() NPE 위험 낮음.
    private User author;
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
    // 부분 수정 메서드 1 - 일정 제목 변경
    public void updateTitle(String title) {
        this.title = title;
    }

    // 부분 수정 메서드 2 - 일정 내용 변경
    public void updateContent(String content) {
        this.content = content;
    }
}

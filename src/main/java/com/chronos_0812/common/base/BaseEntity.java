package com.chronos_0812.common.base;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

/**
 * 모든 엔티티에서 공통적으로 사용하는 생성/수정 시간 추적용 추상 클래스.
 * 'abstract class' 으로 생성
 * 반드시 'abstract' 키워드와 @MappedSuperclass, @EntityListeners를 함께 사용하세요.
 */

@Getter
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public abstract class BaseEntity {      // 공통 부문 common - 작성일, 수정일

    /// 엔티티 최초 저장 시 자동 세팅
    @CreatedBy                              // INSERT 시 자동 세팅
    @Column(updatable = false)              // 수정 불가
    private LocalDateTime createdAt;

    /// 엔티티 수정 시 자동 갱신
    @LastModifiedDate                       // UPDATE 시 자동 갱신
    private LocalDateTime modifiedAt;       /// updatedAt; 와 modifiedAt; 의 차이는??
}

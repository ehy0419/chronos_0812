package com.chronos_0812.common;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Getter
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public abstract class BaseEntity {

    @CreatedBy                              // INSERT 시 자동 세팅
    @Column(updatable = false)              // 수정 불가
    private LocalDateTime createdAt;

    @LastModifiedDate                       // UPDATE 시 자동 갱신
    private LocalDateTime modifiedAt;
}

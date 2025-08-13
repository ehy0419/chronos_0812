package com.chronos_0812.common.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

/**
 * JPA Auditing 활성화 설정.
 * BaseEntity의 @CreatedDate, @LastModifiedDate 필드가 자동 세팅되도록 합니다.
 */

@Configuration
@EnableJpaAuditing
public class JpaAuditingConfig {}
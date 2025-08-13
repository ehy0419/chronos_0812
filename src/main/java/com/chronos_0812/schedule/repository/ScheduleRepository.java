package com.chronos_0812.schedule.repository;

import com.chronos_0812.schedule.entity.Schedule;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * JPA 기본 CRUD 메서드 제공 - JpaRepository를 상속하여,
 * save, findById, findAll, deleteById 등 + 사용자별 조회
 */

public interface ScheduleRepository extends JpaRepository<Schedule, Long> {

    // 추가: 작성자(user) 식별자로 일정 목록 조회 - 작성자 식별자 = UserId userId
    // 수정 전 : (없음)
    // 수정 후 : 필요 시 작성자 필터링에 사용
    List<Schedule> findByUserId(Long userId);
}

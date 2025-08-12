package com.chronos_0812.schedule.repository;

import com.chronos_0812.schedule.entity.Schedule;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * JPA 기본 CRUD 메서드 제공:
 * save, findById, findAll, deleteById 등
 */

public interface ScheduleRepository extends JpaRepository<Schedule, Long> {
    /// JpaRepository를 상속하여 CRUD 기본 메서드 제공 (findAll, findById, save, deleteById 등)
}

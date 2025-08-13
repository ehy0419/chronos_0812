package com.chronos_0812.user.repository;

import com.chronos_0812.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * 기본 CRUD + 이메일 중복 체크 용도
 */

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
    Optional<User> findByEmailAndPassword(String email, String password);
    boolean existsByEmail(String email);

    /**
     * 추가하면 좋을 것들
     * 본인 제외 중복 이메일이 있는 검사
     * boolean existByEmailAndIdNot(String email, Long id);
     */
}
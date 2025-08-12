package com.chronos_0812.user.repository;

import com.chronos_0812.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}

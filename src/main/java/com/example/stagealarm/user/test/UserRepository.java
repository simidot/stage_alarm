package com.example.stagealarm.user.test;

import com.example.stagealarm.user.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<UserEntity, Long> {
  boolean existsByLoginId(String loginId);

  Optional<UserEntity> findByLoginId(String loginId);
}

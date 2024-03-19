package com.example.stagealarm.user.repo;

import com.example.stagealarm.user.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<UserEntity, Long> {
}

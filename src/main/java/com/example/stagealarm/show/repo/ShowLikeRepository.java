package com.example.stagealarm.show.repo;

import com.example.stagealarm.show.entity.ShowInfo;
import com.example.stagealarm.show.entity.ShowLike;
import com.example.stagealarm.user.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ShowLikeRepository extends JpaRepository<ShowLike, Long> {
    Optional<ShowLike> findByUserEntityAndShowInfo(UserEntity user, ShowInfo showInfo);
}

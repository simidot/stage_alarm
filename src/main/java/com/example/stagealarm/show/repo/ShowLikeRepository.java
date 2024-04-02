package com.example.stagealarm.show.repo;

import com.example.stagealarm.show.entity.ShowInfo;
import com.example.stagealarm.show.entity.ShowLike;
import com.example.stagealarm.user.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface ShowLikeRepository extends JpaRepository<ShowLike, Long> {
    Optional<ShowLike> findByUserEntityAndShowInfo(UserEntity user, ShowInfo showInfo);

    @Query("SELECT l FROM ShowLike l WHERE l.showInfo.id = :showInfoId AND l.userEntity.id = :userId")
    ShowLike findByShowInfoIdAndUserId(@Param("showInfoId") Long showInfoId, @Param("userId") Long userId);

}

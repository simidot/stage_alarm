package com.example.stagealarm.alarm.repo;

import com.example.stagealarm.alarm.entity.Alert;
import com.example.stagealarm.show.entity.ShowInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AlertRepository extends JpaRepository<Alert, Long> {

  Optional<Alert> findByUserEmailAndShowInfoId(String userEmail, Long showInfoId);

  List<Alert> findByShowInfoId(Long showInfoId);

}

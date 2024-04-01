package com.example.stagealarm.image.repo;

import com.example.stagealarm.image.entity.Image;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ImageRepository extends JpaRepository<Image, Long> {

  List<Image> findAllByBoard_Id(Long boardId);
}

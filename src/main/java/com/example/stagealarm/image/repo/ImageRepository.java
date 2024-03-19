package com.example.stagealarm.image.repo;

import com.example.stagealarm.image.entity.Image;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ImageRepository extends JpaRepository<Image, Long> {

}

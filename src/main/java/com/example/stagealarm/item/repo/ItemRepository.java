package com.example.stagealarm.item.repo;

import com.example.stagealarm.item.entity.Item;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ItemRepository extends JpaRepository<Item, Long> {

    List<Item> findAllByShowInfoId(Long showInfoId);

}

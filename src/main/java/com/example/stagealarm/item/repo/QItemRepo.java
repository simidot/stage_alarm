package com.example.stagealarm.item.repo;

import com.example.stagealarm.item.entity.Item;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface QItemRepo {
  Page<Item> findAll(Pageable pageable);

  Page<Item> findAllByShowInfoId(Long showInfoId, Pageable pageable);
}

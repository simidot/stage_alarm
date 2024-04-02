package com.example.stagealarm.order.repo;

import com.example.stagealarm.order.entity.ItemOrder;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderRepository extends JpaRepository<ItemOrder, Long> {
    List<ItemOrder> findByUserIdOrderByIdDesc(Long userId);
}
